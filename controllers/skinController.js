const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const { nanoid } = require('nanoid');


exports.getSkins = async (req, res) => {
    try {
        const skins = await prisma.SKIN.findMany();
        res.status(200).json({
            status: true,
            message: "Skins retrieved successfully",
            data: skins
        });
    } catch (error) {
        console.error(error); // Log the error for debugging purposes
        res.status(500).json({
            status: false,
            message: "An unexpected error occurred while retrieving skins",
        });
    }
};

exports.getSkinById = async (req, res) => {
    const SKINID = req.params.SKINID;

    const skin = await prisma.SKIN.findUnique({
        where: {
            SKINID: SKINID
        }
    });

    if (!skin) {
        return res.status(404).json({
            "status": false,
            "message": "Skin not found"
        });
    }

    res.status(200).json({
        "status": true,
        "message": "Skin retrieved successfully",
        "data": skin
    });
}

exports.uploadSkinImage = async (req, res) => {
    if (!req.file || !req.file.cloudStoragePublicUrl) {
        return res.status(400).json({
            "status": false,
            "message": "Image upload failed",
        });
    }

    const skinImageUrl = req.file.cloudStoragePublicUrl;
    let newSkinId;

    // Generate a unique SKINID that is not already in use
    while (true) {
        newSkinId = nanoid(5);
        const existingSkin = await prisma.sKIN.findUnique({
            where: { SKINID: newSkinId }
        });
        if (!existingSkin) break;
    }

    try {
        const newSkin = await prisma.sKIN.create({
            data: {
                SKINID: newSkinId,
                SKINIMG: skinImageUrl,
            },
        });

        res.status(200).json({
            "status": true,
            "message": "Image uploaded and skin record created successfully",
            "data": newSkin,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        });
    }
};