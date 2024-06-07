const predict = require('../modules/model');
const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

exports.predict = async (req, res) => {
    const image = req.file;
    if (!image) {
        return res.status(400).json({
            status: false,
            message: "Invalid input"
        });
    }
    try {
        const result = await predict.predict(image.buffer);
        const skinType = result.label;

        // Save the skin type to the user's profile (assuming user is authenticated and userId is available in req.user.id)
        await prisma.user.update({
            where: { USERID: req.user.id },
            data: { skinType: skinType }
        });

        // Fetch recommended products based on the skin type
        const recommendedProducts = await prisma.product.findMany({
            where: { skinType: skinType }
        });

        return res.status(200).json({
            status: true,
            message: "Scan successful",
            result: result,
            recommendedProducts: recommendedProducts
        });
    } catch (err) {
        console.error(err);
        return res.status(500).json({
            status: false,
            message: "An unexpected error occurred on the server",
            err: err.toString(),
        });
    }
};