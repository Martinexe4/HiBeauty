buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.google.services) apply false
}