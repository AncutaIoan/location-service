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
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.data:spring-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")


    // Caching library (Infinispan)
//    implementation("org.infinispan:infinispan-core:15.2.1.Final")
//    implementation("org.infinispan:infinispan-spring-boot3-starter-embedded:15.2.1.Final")
//    implementation("org.infinispan:infinispan-spring-boot-starter-embedded:14.0.34.Final") // sth weird about this, doesnt work to create an application level cache manager?
    // Caching Library (Redis) changed from infinispan due to spikes during indexing + bloom filter impl
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.redisson:redisson:3.44.0")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}



tasks.withType<Test> {
    useJUnitPlatform()
}
