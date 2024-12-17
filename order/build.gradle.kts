plugins {
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
}
dependencies {
    implementation(project(":payment"))
    implementation(project(":inventory"))
    implementation(project(":delivery"))
}
