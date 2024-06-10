const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

exports.savePredictions = async (req, res) => {
    const { skinId, predictions } = req.body;

    if (!skinId || !predictions || !Array.isArray(predictions)) {
        return res.status(400).json({
            status: false,
            message: "Invalid input data",
        }); 
    }

    try {
        // Check if skin exists
        const skin = await prisma.sKIN.findUnique({
            where: { SKINID: skinId },
        });

        if (!skin) {
            return res.status(404).json({
                status: false,
                message: "Skin not found",
            });
        }

        // Insert predictions
        const predictionPromises = predictions.map(prediction => {
            const { id, percentage } = prediction;
            return prisma.skinRecomendation.create({
                data: {
                    skinId: skinId,
                    recommendationId: id,
                    percentage: percentage,
                },
            });
        });

        await Promise.all(predictionPromises);

        res.status(200).json({
            status: true,
            message: "Prediction results saved successfully",
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An unexpected error occurred on the server",
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