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
        implementation("io.temporal:temporal-sdk:${DependencyVersion.TEMPORAL_SDK}")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // spring boot
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web:${DependencyVersion.SPRING_BOOT_WEB}")

        /** logger */
        implementation("io.github.oshai:kotlin-logging-jvm:${DependencyVersion.KOTLIN_LOGGING}")

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
