'use strict'
const {Storage} = require('@google-cloud/storage')
const fs = require('fs')
const { nanoid } = require("nanoid");

const path = require('path');

const pathKey = path.resolve('./serviceaccountkey.json')

// TODO: Sesuaikan konfigurasi Storage
const gcs = new Storage({
    projectId: process.env.PROJECT_ID,
    keyFilename: pathKey
})

// TODO: Tambahkan nama bucket yang digunakan
const bucketName = process.env.BUCKET_NAME
const bucket = gcs.bucket(bucketName)

function getPublicUrl(filename) {
    return 'https://storage.googleapis.com/' + bucketName + '/' + filename;
}



exports.uploadToGcs = (req, res, next) => {
    if (!req.file) return next()

    const gcsname = nanoid(8);
    const file = bucket.file(gcsname)

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    })

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        next(err)
    })

    stream.on('finish', () => {
        req.file.cloudStorageObject = gcsname
        req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
        // res.send(req.file.cloudStoragePublicUrl)
    })

    stream.end(req.file.buffer)
   
}

exports.viewImgSpecific = (req, res, next) => {
    const file = req.params.imageid
    var stream = bucket.file(file).createReadStream()
    stream.on('data', function (data) {
        res.write(data);
      });
    
      stream.on('error', function (err) {
        console.log('error reading stream', err);
      });
    
      stream.on('end', function () {
        res.end();
      });

}

