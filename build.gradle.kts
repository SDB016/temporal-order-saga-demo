plugins {
    val kotlinVersion = "1.9.25"

    kotlin("jvm") version kotlinVersion
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.demo.temporal"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    dependencies {
        // Temporal Core SDK
        implementation("io.temporal:temporal-sdk:${DependencyVersion.TEMPORAL_SDK}")

        // Spring Boot
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web:${DependencyVersion.SPRING_BOOT_WEB}")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        /** logger */
        implementation("io.github.oshai:kotlin-logging-jvm:${DependencyVersion.KOTLIN_LOGGING}")

        // Test Dependencies
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.temporal:temporal-testing:1.24.1")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}
