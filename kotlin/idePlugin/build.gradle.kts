plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.intellijPlatform)
}

group = "com.kracubo"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(project(":api"))

    implementation(libs.ktorCore)
    implementation(libs.ktorNetty)
    implementation(libs.ktorWebsockets)
    implementation(libs.ktorContentNegotation)
    implementation(libs.ktorSerialization)
    implementation(libs.ktorCors)

    implementation(libs.kotlinxCoroutines)

    implementation(libs.kotlinxSerialization)

    intellijPlatform {
        create("PC", "2025.2")

        bundledPlugin("PythonCore")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "232"
            untilBuild = "252.*"
        }
    }
}