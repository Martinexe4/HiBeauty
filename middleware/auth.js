const { PrismaClient } = require("@prisma/client")
const prisma = new PrismaClient()
const bcrypt = require("bcrypt")
const jwt = require("jsonwebtoken")


const createToken = (payload) => {
    return jwt.sign({ payload }, process.env.SECRET_KEY, {
        expiresIn: 30 * 24 * 60 * 60
    })
}


exports.register = async (req, res) => {

    const { username, email, password, confirmpassword } = req.body

    if (password !== confirmpassword) {
        return res.status(400).json({
            "status": false,
            "message": "Passwords do not match",
        })
    }

    //check if email already exists
    const exists = await prisma.user.count({
        where: {
            EMAIL: email
        }
     });
    if (exists > 0) {
        return res.status(400).json({
            "status": false,
            "message": "Email already exists",
        })
    }


    //encrypt the password
    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt)

    try {
        const newUser = await prisma.user.create({
            data: {
                EMAIL: email,
                PASSWORD: hashedPassword,
                USERNAME: username
            },
        },
    )
    const token = createToken(newUser.USERID)
    console.log(newUser)
    res.status(200).json({
        "status": true,
        "message": "Account created successfully",
        "data": newUser,
        "token": token
    })
    } catch (error) {
        console.log(error)
        res.status(500).json({
            "status": false,
            "message": "An unexpected error occurred on the server",
        })
    }
    console.log(req.body)

}

exports.getUsers = async (req, res) => {
    const users = await prisma.user.findMany()
    res.status(200).json({
        "status": true,
        "message": "All users",
        "data": users,
    })
}

exports.login = async (req, res) => {
    const { email, password } = req.body

    const user = await prisma.user.findUnique({
        where: {
            EMAIL: email
        }
    })

    if (!user){
        return res.status(400).json({
            "status": false,
            "message": "Invalid email",
        })
    }

    const validPassword = await bcrypt.compare(password, user.PASSWORD)
    if (validPassword){
        const token = createToken(user.USERID)
        res.status(200).json({
            "status": true,
            "message": "Login successful",
            "data":  user.USERNAME,
            "token": token
    }) 
    }
    else{
        res.status(400).json({
            "status": false,
            "message": "Invalid password",
        })
    }
}
