plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm", "org.jetbrains.kotlin.jvm.gradle.plugin", "1.6.10")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "17"
}