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
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-graphql")

    implementation("com.graphql-java-kickstart:graphiql-spring-boot-starter:11.1.0")

    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    implementation("org.infinispan:infinispan-core:15.2.0.Final")

    implementation("org.locationtech.jts:jts-core:1.19.0")

    // PostGIS driver to handle PostGIS types (e.g., PGgeometry)
    implementation("net.postgis:postgis-jdbc:2024.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
