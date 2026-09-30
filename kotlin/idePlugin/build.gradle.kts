plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    id("org.jetbrains.intellij.platform")
    alias(libs.plugins.ksp)
}

group = "com.kracubo"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":api"))
    implementation(libs.mDnsCore)

    compileOnly(libs.autoServiceAnnotations)
    ksp(libs.autoServiceKsp)

    intellijPlatform {
        pycharmCommunity("2025.2")
        bundledPlugin("PythonCore")
    }

    implementation(libs.ktorCore)
    implementation(libs.ktorCio)
    implementation(libs.ktorWebsockets)
    implementation(libs.ktorContentNegotiation)
    implementation(libs.ktorSerialization)
    implementation(libs.ktorCors)

    compileOnly(libs.kotlinxCoroutines)
}

// IntelliJ Platform bundles its own patched kotlinx-coroutines.
// Bundling ours causes classloader conflicts at runtime.
// Strip coroutines from runtime entirely; they come from the IDE.
configurations.all {
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-jdk8")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"
            untilBuild = provider { null }
        }
    }
}