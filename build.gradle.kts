import java.util.*
import java.io.*
val localProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}
val ktorVersion by extra { "1.6.3" }

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "ca.tradejmark.arboreum"
version = "0.0.1"

allprojects {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/tradejmark/arboreum-kotlin")
            credentials {
                username = localProperties["gpr.user"]?.toString() ?: System.getenv("USERNAME")
                password = localProperties["gpr.key"]?.toString() ?: System.getenv("TOKEN")
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")

    implementation("io.ktor:ktor-html-builder:$ktorVersion")

    implementation(project(":arboreum-ktor-core"))
}