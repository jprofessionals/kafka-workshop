plugins {
    java
    kotlin("jvm") version "1.8.20"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.8.0"
    id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "1.11.1"
}

group = "no.jpro.kafkaworkshop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
}

buildscript {
    repositories {
        maven("https://packages.confluent.io/maven/")
        maven("https://jitpack.io")
    }
}

dependencies {
    implementation("org.apache.kafka:kafka-streams:3.4.0")
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("io.confluent:kafka-avro-serializer:7.4.1")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("ch.qos.logback:logback-core:1.2.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation(kotlin("stdlib"))


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.apache.kafka:kafka-streams-test-utils:3.5.1")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

schemaRegistry {
    url = "http://localhost:8081"
    register {
        // merk at "app/" kommer foran, selv om dette skriptet ligger i den katalogen
        subject("C-type", "app/src/main/avro/oppgave3-uniondemo/C.avsc", "AVRO")
        subject("A-type", "app/src/main/avro/oppgave3-uniondemo/A.avsc", "AVRO")
            .addReference("no.jpro.kafkaworkshop.oppgave3union.C", "C-type")
        subject("B-type", "app/src/main/avro/oppgave3-uniondemo/B.avsc", "AVRO")
            .addReference("no.jpro.kafkaworkshop.oppgave3union.C", "C-type")
        subject("uniondemo-value", "app/src/main/avro/oppgave3-uniondemo/union.avsc", "AVRO")
            .addReference("no.jpro.kafkaworkshop.oppgave3union.A", "A-type")
            .addReference("no.jpro.kafkaworkshop.oppgave3union.B", "B-type")
    }
}
