const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.getRecommendations = async (req, res) => {
    try {
        const recommendations = await prisma.recommendation.findMany({
            include: {
                user: true,
                skin: true,
                product: true
            }
        });
        res.status(200).json({
            status: true,
            message: "Recommendations retrieved successfully",
            data: recommendations
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while fetching recommendations"
        });
    }
};

exports.getRecommendationById = async (req, res) => {
    const recommendationId = parseInt(req.params.recommendationId, 10);

    try {
        const recommendation = await prisma.recommendation.findUnique({
            where: {
                id: recommendationId
            },
            include: {
                user: true,
                skin: true,
                product: true
            }
        });

        if (!recommendation) {
            return res.status(404).json({
                status: false,
                message: "Recommendation not found"
            });
        }

        res.status(200).json({
            status: true,
            message: "Recommendation retrieved successfully",
            data: recommendation
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while fetching the recommendation"
        });
    }
};

exports.createRecommendation = async (req, res) => {
    const { recommendationText, userId, skinId, productId } = req.body;

    try {
        const newRecommendation = await prisma.recommendation.create({
            data: {
                recommendationText,
                userId,
                skinId,
                productId
            }
        });

        res.status(201).json({
            status: true,
            message: "Recommendation created successfully",
            data: newRecommendation
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while creating the recommendation"
        });
    }
};

exports.updateRecommendation = async (req, res) => {
    const recommendationId = parseInt(req.params.recommendationId, 10);
    const { recommendationText } = req.body;

    try {
        const updatedRecommendation = await prisma.recommendation.update({
            where: { id: recommendationId },
            data: { recommendationText }
        });

        res.status(200).json({
            status: true,
            message: "Recommendation updated successfully",
            data: updatedRecommendation
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while updating the recommendation"
        });
    }
};

exports.deleteRecommendation = async (req, res) => {
    const recommendationId = parseInt(req.params.recommendationId, 10);

    try {
        await prisma.recommendation.delete({
            where: { id: recommendationId }
        });

        res.status(200).json({
            status: true,
            message: "Recommendation deleted successfully"
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while deleting the recommendation"
        });
    }
};
