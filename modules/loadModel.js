const tf = require('@tensorflow/tfjs-node');
require('dotenv').config();
exports.LoadModel = async () =>{
    return tf.loadLayersModel(process.env.MODEL_URL);
}