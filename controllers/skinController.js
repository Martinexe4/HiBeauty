const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

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

