plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinCompose)
}

android {
    namespace = "com.kracubo.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kracubo.app"
        minSdk = 24
        targetSdk = 36
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":api"))

    implementation(libs.androidxCore)
    implementation(libs.androidxLifecycle)
    implementation(libs.androidxActivity)
    //libs for camera and qrcode
    val cameraVersion = "1.3.1"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")
    implementation(platform(libs.androidxComposeBom))

    implementation(libs.androidxComposeUi)
    implementation(libs.androidxComposeGraphics)
    implementation(libs.androidxComposePreviewTooling)
    implementation(libs.androidxComposeMaterial3)
    implementation(libs.androidxComposeNav)
    implementation(libs.activityKtx)
    implementation(libs.materialIconsOld)

    implementation("com.google.mlkit:barcode-scanning:17.2.0")
}