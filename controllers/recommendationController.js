const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

// Rekomendasi produk berdasarkan jenis kulit
exports.getRecommendations = async (req, res) => {
    const skinType = Number(req.params.id); // Mengambil jenis kulit dari parameter
    try {
        const products = await prisma.product.findMany({
            where: {
                recommendations: skinType
            }
        });

        if (products.length === 0) {
            return res.status(404).json({
                "status": false,
                "message": "No products found for the given skin type"
            });
        }

        res.status(200).json({
            "status": true,
            "message": "Recommendations retrieved successfully",
            "data": products
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        });
    }
};
