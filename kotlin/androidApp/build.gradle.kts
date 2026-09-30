plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.kotlinSerialization)
}

val releaseTaskRequested = gradle.startParameter.taskNames.any { taskName ->
    taskName.contains("Release", ignoreCase = true)
}

val requiredReleaseProperties = listOf(
    "RELEASE_STORE_FILE",
    "RELEASE_STORE_PASSWORD",
    "RELEASE_KEY_ALIAS",
    "RELEASE_KEY_PASSWORD"
)

if (releaseTaskRequested) {
    val missing = requiredReleaseProperties.filter { prop -> !project.hasProperty(prop) }

    if (missing.isNotEmpty()) {
        throw GradleException(
            "Missing release signing properties: ${missing.joinToString(", ")}. " +
                    "Please set them in ~/.gradle/gradle.properties or pass via -P flags."
        )
    }

    val storeFile = rootProject.file(providers.gradleProperty("RELEASE_STORE_FILE").get())

    if (!storeFile.isFile) {
        throw GradleException(
            "Release keystore not found at: ${storeFile.absolutePath}. " +
                    "Check RELEASE_STORE_FILE in ~/.gradle/gradle.properties."
        )
    }
}

android {
    namespace = "com.kracubo.app"
    compileSdk = 36

    signingConfigs {
        create("release") {
            storeFile = rootProject.file(providers.gradleProperty("RELEASE_STORE_FILE").get())
            storePassword = providers.gradleProperty("RELEASE_STORE_PASSWORD").get()
            keyAlias = providers.gradleProperty("RELEASE_KEY_ALIAS").get()
            keyPassword = providers.gradleProperty("RELEASE_KEY_PASSWORD").get()
        }
    }

    defaultConfig {
        applicationId = "com.kracubo.app"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0-Alpha"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

    implementation(platform(libs.androidxComposeBom))

    implementation(libs.androidxCore)
    implementation(libs.androidxLifecycle)
    implementation(libs.androidxActivity)
    implementation(libs.composeMaterial)
    implementation(libs.androidxCameraCore)
    implementation(libs.androidxCamera2)
    implementation(libs.androidxCameraLifecycle)
    implementation(libs.androidxCameraView)

    implementation(libs.ktorClientContentNegotiation)
    implementation(libs.ktorClientCore)
    implementation(libs.ktorClientCio)
    implementation(libs.ktorClientWebsockets)
    implementation(libs.kotlinxCoroutines)
    implementation(libs.ktorSerialization)


    implementation(libs.androidxComposeUi)
    implementation(libs.androidxComposeGraphics)
    implementation(libs.androidxComposeMaterial3)
    implementation(libs.androidxComposeNav)
    implementation(libs.activityKtx)
    implementation(libs.materialIconsOld)

    implementation(libs.mlkit)
}