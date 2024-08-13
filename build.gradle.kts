plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.noarg") version "1.3.71"
    kotlin("plugin.spring") version "1.9.24"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.2")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // QueryDSL 때문에 별도로 추가한 의존성
//    implementation("org.springframework.data:spring-data-mongodb:3.3.0")
//    implementation("com.querydsl:querydsl-mongodb:5.0.0")
//    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
//    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
//    implementation("org.mongodb:mongodb-driver-core:4.4.0")
//    implementation("org.mongodb:mongodb-driver-sync:4.4.0")
}

// QueryDSL 때문에 별도로 추가한 의존성
//allOpen {
//    annotation("org.springframework.data.mongodb.core.mapping.Document")
//    annotation("jakarta.persistence.Entity")
//    annotation("com.querydsl.core.annotations.QueryEntity")
//}
//
//noArg {
//    annotation("org.springframework.data.mongodb.core.mapping.Document")
//    annotation("jakarta.persistence.Entity")
//    annotation("com.querydsl.core.annotations.QueryEntity")
//}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}