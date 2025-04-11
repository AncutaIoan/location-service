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
    // Web and Reactive support
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // GraphQL support
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.graphql-java-kickstart:graphiql-spring-boot-starter:11.1.0")

    // PostgreSQL and R2DBC
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    implementation("org.springframework.data:spring-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    // Caching library (Infinispan)
    implementation("org.infinispan:infinispan-core:15.2.0.Final")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}



tasks.withType<Test> {
    useJUnitPlatform()
}
