import org.gradle.kotlin.dsl.dependencies

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.kotlinxSerialization)
}