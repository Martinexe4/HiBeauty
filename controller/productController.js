const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.getProducts = async (req, res) => {
    try {
        const products = await prisma.product.findMany();
        res.status(200).json({
            status: true,
            message: "Products retrieved successfully",
            data: products
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while fetching products"
        });
    }
};

exports.getProductById = async (req, res) => {
    const productId = parseInt(req.params.productId, 10);

    try {
        const product = await prisma.product.findUnique({
            where: {
                id: productId
            }
        });

        if (!product) {
            return res.status(404).json({
                status: false,
                message: "Product not found"
            });
        }

        res.status(200).json({
            status: true,
            message: "Product retrieved successfully",
            data: product
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while fetching the product"
        });
    }
};

exports.createProduct = async (req, res) => {
    const { name, description, price, skinType } = req.body;

    try {
        const newProduct = await prisma.product.create({
            data: {
                name,
                description,
                price,
                skinType
            }
        });

        res.status(201).json({
            status: true,
            message: "Product created successfully",
            data: newProduct
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while creating the product"
        });
    }
};

exports.updateProduct = async (req, res) => {
    const productId = parseInt(req.params.productId, 10);
    const { name, description, price, skinType } = req.body;

    try {
        const updatedProduct = await prisma.product.update({
            where: { id: productId },
            data: { name, description, price, skinType }
        });

        res.status(200).json({
            status: true,
            message: "Product updated successfully",
            data: updatedProduct
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while updating the product"
        });
    }
};

exports.deleteProduct = async (req, res) => {
    const productId = parseInt(req.params.productId, 10);

    try {
        await prisma.product.delete({
            where: { id: productId }
        });

        res.status(200).json({
            status: true,
            message: "Product deleted successfully"
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({
            status: false,
            message: "An error occurred while deleting the product"
        });
    }
};
