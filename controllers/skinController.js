const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.getSkins = async (req, res) => {
    const skins = await prisma.skin.findMany();
    res.status(200).json({
        "status": true,
        "message": "Skins retrieved successfully",
        "data": skins
    });
}

exports.getSkinById = async (req, res) => {
    const skinId = req.params.skinId;

    const skin = await prisma.skin.findUnique({
        where: {
            SKINID: skinId
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

