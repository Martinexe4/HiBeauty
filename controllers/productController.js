const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

// Mengambil semua produk skincare
exports.getProducts = async (req, res) => {
    try {
        const products = await prisma.product.findMany();
        res.status(200).json({
            "status": true,
            "message": "Products retrieved successfully",
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

// Mengambil produk skincare berdasarkan ID
exports.getProductById = async (req, res) => {
    const productId = req.params.id;
    let Id = Number(productId);
    try {
        const product = await prisma.product.findUnique({
            where: {
                id: Id
            }
        });

        if (!product) {
            return res.status(404).json({
                "status": false,
                "message": "Product not found"
            });
        }

        res.status(200).json({
            "status": true,
            "message": "Product retrieved successfully",
            "data": product
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        });
    }
};
