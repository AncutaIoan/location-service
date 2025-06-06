package angrymiaucino.locationservice.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

@Tag("integration-test")
public abstract class AbstractTestcontainersIntegrationTest {

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final Logger log = LoggerFactory.getLogger(AbstractTestcontainersIntegrationTest.class);

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"))
            .withDatabaseName("testDb")
            .withUsername("root")
            .withPassword("123456")
            .withReuse(true);

    @BeforeAll
    static void setUp() {
        postgres.start();
        log.info("Testcontainers -> PostgresSQL DB started on [{}] with user:root and password:123456", r2dbcUrl());
    }

    protected void executeScriptBlocking(String sqlScript) {
        Mono.from(connectionFactory.create())
                .flatMap(connection -> ScriptUtils.executeSqlScript(connection, new ClassPathResource(sqlScript)))
                .block();
    }

    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", AbstractTestcontainersIntegrationTest::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.r2dbc.pool.enabled", () -> true);
        registry.add("spring.r2dbc.pool.max-size", () -> 50);
    }

    public static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%d/%s", postgres.getHost(),
                postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgres.getDatabaseName());
    }

    public static PostgreSQLContainer<?> getPostgres() {
        return postgres;
    }
}
