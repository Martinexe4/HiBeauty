const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const jwt = require("jsonwebtoken");


const accessValidation = (req, res, next) => {
    const {authorization} = req.headers;

    if (!authorization){
        return res.status(401).json({
            "status": false,
            "message": "Unauthorized access",
        })
    }

    console.log(authorization) //remove later

    const token = authorization.split(" ")[1];
    const secret = process.env.SECRET_KEY;

    console.log(token) //remove later

    try{
        const decoded = jwt.verify(token, secret);
        console.log(decoded.payload) //remove later
        req.user = decoded;
        console.log(req.user) //remove later



        prisma.user.findUnique({
            where: {
                USERID: decoded.payload
            }
        }).then(user => {
            if (!user){
                return res.status(401).json({
                    "status": false,
                    "message": "Unauthorized access",
                })
            }
            next()
        }).catch(error => {
            return res.status(401).json({
                "status": false,
                "message": "Unauthorized access",
            })
        })

    }catch(error){
        return res.status(401).json({
            "status": false,
            "message": "Unauthorized access",
        })
    }
}

module.exports = accessValidation;