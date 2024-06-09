const express = require("express");
const router = express.Router();
const auth = require("../middleware/auth");
const accessValidation = require("../middleware/accessValidation");
const skinController = require("../controllers/skinController");
const productController = require("../controllers/productController");
const modelController = require("../controllers/modelController");
const userController = require("../controllers/userController");
const recommendationController = require("../controllers/recommendationController");
const skinRecommendationController = require("../controllers/skinRecommendationController");
const images = require("../modules/images");
const Multer = require('multer');
const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 3 * 1024 * 1024
});

router.get("/", (req, res) => { 
    res.status(200).json({
        "message": "Welcome to HiBeauty! API server. We recommend that you first register and login before accessing our endpoints."
    });
});

// Auth routes
router.post('/register', auth.register);
router.post('/login', auth.login);
router.get('/users', accessValidation, auth.getUsers);


// Skin routes
router.get('/skins', accessValidation, skinController.getSkins);
router.get('/skin/:SKINID', accessValidation, skinController.getSkinById);

// Product routes
router.get('/products', accessValidation, productController.getProducts);
router.get('/products/:id', accessValidation, productController.getProductById);


// Recommendation routes
router.get('/recommendations/:id', accessValidation, recommendationController.getRecommendations);

// Prediction routes (POST)
router.post('/predictions', skinRecommendationController.postPredictions);

// Machine Learning routes
router.post("/predict", accessValidation, multer.single('attachment'), modelController.predict );
router.get("/predict/:id", accessValidation, multer.single('attachment'), modelController.getSkinTypeById );

// User routes
router.put('/user/:userId', accessValidation, multer.single('IMAGE'), images.uploadToGcs, userController.updateProfile)
router.get('/users', accessValidation, auth.getUsers )

module.exports = router;
