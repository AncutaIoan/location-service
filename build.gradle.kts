plugins {
    java
    id("org.springframework.boot") version "3.3.10"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "angrymiaucino"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    // GRAPHQL playground
    implementation("com.graphql-java-kickstart:graphiql-spring-boot-starter:11.1.0")

    runtimeOnly("org.postgresql:postgresql")

    // Hibernate Spatial for PostGIS support
    implementation("org.hibernate:hibernate-spatial:6.2.7.Final") // Adjust to your Hibernate version

    // JTS Topology Suite for geometry (point) handling
    implementation("org.locationtech.jts:jts-core:1.19.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
