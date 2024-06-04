const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.getSkins = async (req, res) => {
    const skins = await prisma.SKIN.findMany();
    res.status(200).json({
        "status": true,
        "message": "Skins retrieved successfully",
        "data": skins
    });
    // add erorhandling
}

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

