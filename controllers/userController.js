const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

exports.updateProfile = async (req, res) => {
    const { USERNAME } = req.body;
    const { userId } = req.params;

    const user = await prisma.user.findUnique({
        where: {
            USERID: userId
        },
    });
    console.log(user);

    if (!user) {
        return res.status(404).json({
            "status": false,
            "message": "User not found",
        });
    }

    let imageUrl = user.PROFILEIMG;
    if (req.file && req.file.cloudStoragePublicUrl) {
        imageUrl = req.file.cloudStoragePublicUrl;
    }

    try {
        const updatedUser = await prisma.user.update({
            where: {
                USERID: userId
            },
            data: {
                USERNAME: USERNAME,
                PROFILEIMG: imageUrl,
                UPDATEDAT: new Date()
            }
        });
        res.status(200).json({
            "status": true,
            "message": "User profile updated successfully",
            "data": updatedUser,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        });
    }
};
