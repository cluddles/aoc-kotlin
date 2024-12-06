group = "com.cluddles"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testApi(libs.kotlin.test.junit5)
    testApi(libs.junit.jupiter.engine)
    testApi(libs.junit.jupiter.params)
    testApi(libs.assertj.core)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
