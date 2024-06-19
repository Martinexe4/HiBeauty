/*
  Warnings:

  - Added the required column `link` to the `Product` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE `product` ADD COLUMN `link` VARCHAR(191) NOT NULL;
