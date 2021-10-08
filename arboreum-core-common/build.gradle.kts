plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ca.tradejmark.arboreum"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0-RC")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvm()
        js().browser()
    }
}