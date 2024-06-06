plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.redy.cpv2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.redy.cpv2"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.firestore)

    implementation(kotlin("stdlib"))
    implementation(libs.androidx.appcompat.v140)
    implementation(libs.androidx.constraintlayout.v212)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.material.v140)
    implementation(libs.androidx.core.ktx.v170)
    implementation(libs.androidx.viewpager2.v100)
    implementation(libs.androidx.fragment.ktx)

    //firebase
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))

    //onboarding
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //scan
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //cameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    //news
    implementation(libs.shimmer)

    //personalization
    implementation(libs.androidx.viewpager2.v100)

    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit.v113)
    androidTestImplementation(libs.androidx.espresso.core.v340)

    //home
    implementation(libs.androidx.navigation.fragment.ktx.v274)
    implementation(libs.androidx.navigation.ui.ktx.v274)

    //scan
    implementation (libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.tensorflow.lite.metadata)
    implementation(libs.tensorflow.tensorflow.lite.task.vision)
    implementation(libs.androidx.room.runtime)

    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)

    //error camera activity
    implementation(libs.guava)

}