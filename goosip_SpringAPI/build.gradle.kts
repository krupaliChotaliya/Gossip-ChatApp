plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.chatsapp"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

//  firabase
    implementation ("com.google.firebase:firebase-admin:9.2.0")
    implementation("io.perfmark:perfmark-api:0.25.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
