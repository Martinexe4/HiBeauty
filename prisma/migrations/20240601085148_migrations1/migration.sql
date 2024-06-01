-- CreateTable
CREATE TABLE `SKIN` (
    `SKINID` CHAR(5) NOT NULL,
    `SKINNAME` VARCHAR(50) NOT NULL,
    `SKINDESC` VARCHAR(200) NOT NULL,
    `SKINIMG` VARCHAR(255) NOT NULL,

    PRIMARY KEY (`SKINID`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `User` (
    `USERID` CHAR(16) NOT NULL,
    `USERNAME` VARCHAR(25) NOT NULL,
    `EMAIL` VARCHAR(200) NOT NULL,
    `PASSWORD` VARCHAR(255) NOT NULL,
    `PROFILEIMG` VARCHAR(255) NULL,
    `CREATEDAT` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `UPDATEDAT` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `skinId` VARCHAR(191) NULL,

    UNIQUE INDEX `User_EMAIL_key`(`EMAIL`),
    PRIMARY KEY (`USERID`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `Product` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(200) NOT NULL,
    `price` DOUBLE NOT NULL,
    `skinType` VARCHAR(50) NOT NULL,
    `skinId` VARCHAR(191) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `Recommendation` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `recommendationText` VARCHAR(255) NOT NULL,
    `userId` VARCHAR(191) NOT NULL,
    `skinId` VARCHAR(191) NOT NULL,
    `productId` INTEGER NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
