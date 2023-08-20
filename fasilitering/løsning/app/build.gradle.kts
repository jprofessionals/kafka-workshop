plugins {
    java
    kotlin("jvm") version "1.8.20"
}

group = "no.jpro.kafkaworkshop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka-streams:3.4.0")
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("ch.qos.logback:logback-core:1.2.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation(kotlin("stdlib"))


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "18"
    }
}