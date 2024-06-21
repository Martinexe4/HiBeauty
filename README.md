# Backend API Documentation 🧑‍💻

## API URL 🔗

[HiBeauty API]

```http
  http://localhost:3000/
```

## How to run this API on your local machine 💻

If you want to run this API Server on your local machine, you need to do this steps:

- First, clone this repository. `git clone https://github.com/Martinexe4/HiBeauty.git`
- Second, open terminal and go to this project's root directory.
- Third, type `npm install` in your terminal and hit enter button.
- Fourth, start xampp.
- fifth, create database name `hibeauty`.
- sixth, type `npx prisma generate` in your terminal and hit enter button.	
- seventh, type `npx prisma migrate dev` in your terminal and hit enter button.
- eighth, type `npm run start-dev` in your terminal and hit enter button.
- Finally, the server will run on your http://localhost:3000

# API Endpoints

## Auth Endpoints
### Register

```http
  POST /register
```
Register account for user

| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `username`        | `string` | **Required**. |
| `email`           | `string` | **Required**. |
| `password`        | `string` | **Required**. |
| `confirmpassword` | `string` | **Required**. |

#### Response

```http
  }
  "status": "true",
  "message": "Account created successfully",
   "data": {
        "USERID": ,
        "USERNAME": ,
        "EMAIL": ,
        "PASSWORD": ,
        "PROFILEIMG": ,
        "CREATEDAT":,
        "UPDATEDAT": 
    },
    "token": 
   }
```

### Login
Login for user

```http
  POST /login
```
| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `email`           | `string` | **Required**. |
| `password`        | `string` | **Required**. |

#### Response
```http
  }
  "status": "true",
  "message": "Login successful",
   "data": {
    	“USERNAME”:,
    },
    "token": 
   }
```

## User Endpoint
### Get All User
Get All User

```http
  GET /users
```
#### Authorization
bearer token: token

#### Response
```http
  }
  "status": “true”,
 "message": "All users",
 "data”:{
	“USERID”:,
	“USERNAME”:,
	“EMAIL”:,
	“PASSWORD”:,
	“PROFILEIMG”:,
	“AGE”:,
	“GENDER”:,
	“CREATEDAT”:,
	“UPDATEDAT”:,
	“skinId”:,
	“skins”:
 }
 }
```

### Add Gender and Age
Add Gender and Age

```http
   POST /user/{USER ID}/age-gender
```
Add gender and age
| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `age`     		 | `integer` | **Required**. |
| `gender`           | `string` | **Required**. |

#### Authorization
bearer token: token

### Response
```http
 }
 “status”:”true”
 “message”:"User age and gender updated successfully"
 “data”:{
	”AGE”:,
	“GENDER”:,
	“UPDATEDAT”:
 }
}
```

### Update Profile Picture by ID
Update Profile Picture by ID

```http
   PUT /user/{USER ID}
```
Update Profile Picture by ID
| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `userID`     	 | `string` | **Required**. |
| `PROFILEIMG`       |`file` | **Required**. |

#### Authorization
bearer token: token

#### Response
```http
}
 "status": true,
 "message": "User profile updated successfully",
 "data”:{
	“USERNAME”:,
	“PROFILEIMG”:,
	“UPDATEDAT”:
 }
}
```

### Get User by ID
Get User Profile by ID

```http
  GET /user{USERID}/profile
```
#### Authorization
bearer token: token

#### Response
```http
}
 "status": true,
 "message": "User profile retrieved successfully",
 "data”:{
	“USERNAME”:,
	“AGE”:,
	“GENDER”:
 }
}
```

### Get Profile Image by ID
Get Profile Image by ID

```http
  GET /user{USERID}/profile-image
```
#### Authorization
bearer token: token

#### Response
```http
}
 "status":“true”,
 "message":"User profile image retrieved successfully",
 "data”:{
	“USERID”:,
	“PROFILEIMG”:
 }
}
```

### Upload Skin Image
Upload Skin Image 

```http
   POST /skin/upload
```
Upload Skin Image
| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `SKINID`           | `string` | **required**. |
| `SKINIMG`           | `string` | **required**. |

#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message":"Image uploaded and skin record created successfully",
  "data”:{
	“SKINID”:,
	“SKINIMG”:
 }
}
``` 

## Skin Endpoints
### Get All Skin
Get All Skin

```http
   GET /skins
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message":"Skins retrieved successfully",
  "data”:{
	“SKINID”:,
	“SKINIMG”:,
	“UserSkin”:{
		“users”:
 	}
	“SkinRecomendation”:{
		“recommendations”:
	}
 }	
}
``` 
### Get Skin by ID
Get Skin by ID

```http
   GET /skins/{SKIN ID}
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message":"Skin retrieved successfully",
  "data”:{
	“SKINID”:,
	“SKINIMG”:
 }
}
```

## Product Endpoints
### Get All Products
Get All Products

```http
   GET /products
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message”:”Products retrieved successfully",
  "data”:{
 	“id”:,
	“name”:,
	“description”:,
	“ingridients”:,
	“link”:,
	“recomId”:,
	“recommendation”:,
	“typeId”:,
	“typeProd”:
 }
}
```

### Get Product by ID
Get Product by ID

```http
   GET /products/{PRODUCT ID}
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message”:”Products retrieved successfully",
  "data”:{
	“id”:,
	“name”:,
	“description”:,
	“ingridients”:,
	“link”:,
	“recomId”:,
	“recommendation”:,
	“typeId”:,
	“typeProd”:
 }
}
```

## Recommendation Endpoint
### Get Recommendation by ID
Get Recommendation by ID

```http
   GET /recommendations/{RECOMMENDATION ID}
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message”:”Recommendations retrieved successfully",
  "data”:{
	“skinID”:,
	“skinRecommendations”:{
		“recommendation”.maxPercentageRecommendation,
		}
	“products”.filteredProducts:
 }
}
```

## Machine Learning Endpoint
### Get Prediction by ID
Get Prediction by ID

```http
   GET /predict/{PREDICTION ID}
```
#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message”:”Skin retrieved successfully",
  "data”:{
	“SKINID”:,
	“skinRecommendations”:
 }
}
```

## Prediction Endpoint
### Post Predictions
Post Predictions

```http
   POST /predictions
```
Upload Skin Image
| Parameter         | Type     | Description   |
| :------------------ | :--------- | :-------------- |
| `skinID`           | `string` | **required**. |
| `recommendationID`           | `integer` | **required**. |
| `percentage`           | `float` | **required**. |

#### Authorization
bearer token: token

#### Response
```http
}
  "status”:”true”,
  "message”:”Prediction results saved successfully",
  "data”:{
	“skinID”:,
	“recommendationID”:,
	“percentage”:
 }
}
```
