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

exports.getProfileImage = async (req, res) => {
    const { userId } = req.params;

    try {
        const user = await prisma.user.findUnique({
            where: {
                USERID: userId
            },
            select: {
                PROFILEIMG: true
            }
        });

        if (!user) {
            return res.status(404).json({
                "status": false,
                "message": "User not found",
            });
        }

        res.status(200).json({
            "status": true,
            "message": "User profile image retrieved successfully",
            "data": {
                profileImage: user.PROFILEIMG
            }
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        });
    }
};


exports.addAgeAndGender = async (req, res) => {
    const { AGE, GENDER } = req.body;
    const { userId } = req.params;

    try {
        const user = await prisma.user.findUnique({
            where: {
                USERID: userId
            },
        });

        if (!user) {
            return res.status(404).json({
                "status": false,
                "message": "User not found",
            });
        }

        const updatedUser = await prisma.user.update({
            where: {
                USERID: userId
            },
            data: {
                AGE: AGE,
                GENDER: GENDER,
                UPDATEDAT: new Date()
            }
        });

        res.status(200).json({
            "status": true,
            "message": "User age and gender updated successfully",
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


exports.getUserProfile = async (req, res) => {
    const { userId } = req.params;

    try {
        const user = await prisma.user.findUnique({
            where: {
                USERID: userId
            },
            select: {
                USERNAME: true,
                AGE: true,
                GENDER: true
            }
        });

        if (!user) {
            return res.status(404).json({
                status: false,
                message: "User not found",
            });
        }

        res.status(200).json({
            status: true,
            message: "User profile retrieved successfully",
            data: user,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An unexpected error occurred on the server",
        });
    }
};
