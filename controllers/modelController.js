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

        // Simpan jenis kulit ke model Recommendation
        const recommendation = await prisma.recommendation.create({
            data: {
                skinType: skinType
            }
        });

        // Fetch recommended products based on the skin type
        const recommendedProducts = await prisma.product.findMany({
            where: {
                recommendationId: recommendation.id // Ambil produk berdasarkan rekomendasi yang baru dibuat
            }
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

exports.getSkinTypeById = async (req, res) => {
    const SKINID = req.params.id;

    try {
        const skin = await prisma.sKIN.findUnique({
            where: {
                SKINID: SKINID,
            },
            include: {
                recommendations: {
                    include: {
                        recommendation: true,
                    }
                }
            }
        });

        if (!skin) {
            return res.status(404).json({
                status: false,
                message: "Skin not found",
            });
        }

        // Extracting all related skinTypes and percentages
        const skinRecommendations = skin.recommendations.map(rec => ({
            skinType: rec.recommendation.skinType,
            percentage: rec.percentage
        }));

        res.status(200).json({
            status: true,
            message: "Skin retrieved successfully",
            data: skinRecommendations,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while retrieving the skin data",
        });
    }
};
