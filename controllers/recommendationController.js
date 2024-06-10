const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.getRecommendations = async (req, res) => {
    const skinId = req.params.id; // Mengambil skin ID dari parameter

    try {
        // Mengambil persentase skinType dari model SkinRecommendation
        const skinRecommendations = await prisma.skinRecomendation.findMany({
            where: {
                skinId: skinId
            },
            include: {
                recommendation: true // Mengambil data dari tabel Recommendation
            }
        });

        // Cek apakah ada rekomendasi skin type
        if (skinRecommendations.length === 0) {
            return res.status(404).json({
                status: false,
                message: "No skin recommendations found for the given skin ID"
            });
        }

        // Mengambil rekomendasi dengan persentase tertinggi
        const maxPercentageRecommendation = skinRecommendations.reduce((max, recommendation) => {
            return recommendation.percentage > max.percentage ? recommendation : max;
        });

        // Mengambil produk yang sesuai dengan rekomendasi skin type tertinggi
        const filteredProducts = await prisma.product.findMany({
            where: {
                recomId: maxPercentageRecommendation.recommendation.id // Menyaring produk berdasarkan rekomendasi ID
            }
        });

        // Jika tidak ada produk yang ditemukan
        if (filteredProducts.length === 0) {
            return res.status(404).json({
                status: false,
                message: "No products found for the given skin type"
            });
        }

        res.status(200).json({
            status: true,
            message: "Recommendations retrieved successfully",
            data: filteredProducts
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An unexpected error occurred on the server",
        });
    }
};
