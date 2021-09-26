plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ca.tradejmark.arboreum"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("ca.tradejmark.arboreum:arboreum-kotlin:0.0.2")

    val ktorVersion = "1.6.3"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0-RC")
}