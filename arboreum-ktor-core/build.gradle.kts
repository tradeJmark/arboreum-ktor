import java.util.*
import java.io.*
val localProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

plugins {
    kotlin("jvm")
}

group = "ca.tradejmark.arboreum"
version = "0.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/tradejmark/arboreum-kotlin")
        credentials {
            username = localProperties["gpr.user"]?.toString() ?: System.getenv("USERNAME")
            password = localProperties["gpr.key"]?.toString() ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    api("ca.tradejmark.arboreum:arboreum-kotlin:0.0.1")
}