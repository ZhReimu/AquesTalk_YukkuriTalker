import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "me.administrator"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/net.java.dev.jna/jna
    implementation("net.java.dev.jna:jna:5.10.0")
    // https://mvnrepository.com/artifact/org.apache.lucene/lucene-analyzers-kuromoji
    // implementation("org.apache.lucene:lucene-analyzers-kuromoji:8.11.1")
    // https://mvnrepository.com/artifact/com.atilika.kuromoji/kuromoji-ipadic
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}