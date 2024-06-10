/*
  Warnings:

  - You are about to drop the column `price` on the `product` table. All the data in the column will be lost.
  - You are about to drop the column `skinId` on the `product` table. All the data in the column will be lost.
  - You are about to drop the column `skinType` on the `product` table. All the data in the column will be lost.
  - You are about to drop the column `productId` on the `recommendation` table. All the data in the column will be lost.
  - You are about to drop the column `skinId` on the `recommendation` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `recommendation` table. All the data in the column will be lost.
  - You are about to drop the column `SKINDESC` on the `skin` table. All the data in the column will be lost.
  - You are about to drop the column `SKINNAME` on the `skin` table. All the data in the column will be lost.
  - Added the required column `skinType` to the `Recommendation` table without a default value. This is not possible if the table is not empty.

*/
-- DropForeignKey
ALTER TABLE `product` DROP FOREIGN KEY `Product_skinId_fkey`;

-- DropForeignKey
ALTER TABLE `recommendation` DROP FOREIGN KEY `Recommendation_productId_fkey`;

-- DropForeignKey
ALTER TABLE `recommendation` DROP FOREIGN KEY `Recommendation_skinId_fkey`;

-- DropForeignKey
ALTER TABLE `recommendation` DROP FOREIGN KEY `Recommendation_userId_fkey`;

-- DropIndex
DROP INDEX `User_skinId_fkey` ON `user`;

-- AlterTable
ALTER TABLE `product` DROP COLUMN `price`,
    DROP COLUMN `skinId`,
    DROP COLUMN `skinType`,
    ADD COLUMN `ingridients` VARCHAR(191) NOT NULL DEFAULT '',
    ADD COLUMN `recomId` INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN `typeId` INTEGER NOT NULL DEFAULT 0;

-- AlterTable
ALTER TABLE `recommendation` DROP COLUMN `productId`,
    DROP COLUMN `skinId`,
    DROP COLUMN `userId`,
    ADD COLUMN `skinType` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `skin` DROP COLUMN `SKINDESC`,
    DROP COLUMN `SKINNAME`;

-- CreateTable
CREATE TABLE `ProductType` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `type` VARCHAR(191) NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `SkinRecomendation` (
    `skinId` VARCHAR(191) NOT NULL,
    `recommendationId` INTEGER NOT NULL,
    `percentage` DOUBLE NOT NULL,

    PRIMARY KEY (`skinId`, `recommendationId`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `Product` ADD CONSTRAINT `Product_recomId_fkey` FOREIGN KEY (`recomId`) REFERENCES `Recommendation`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `Product` ADD CONSTRAINT `Product_typeId_fkey` FOREIGN KEY (`typeId`) REFERENCES `ProductType`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `SkinRecomendation` ADD CONSTRAINT `SkinRecomendation_skinId_fkey` FOREIGN KEY (`skinId`) REFERENCES `SKIN`(`SKINID`) ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE `SkinRecomendation` ADD CONSTRAINT `SkinRecomendation_recommendationId_fkey` FOREIGN KEY (`recommendationId`) REFERENCES `Recommendation`(`id`) ON DELETE RESTRICT ON UPDATE CASCADE;
