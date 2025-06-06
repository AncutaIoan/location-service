package angrymiaucino.locationservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

    private final boolean repairRequired;

    public FlywayConfig(@Value("${spring.flyway.repair-required:false}") boolean repairRequired) {
        this.repairRequired = repairRequired;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties properties) {
        Flyway flyway = Flyway.configure()
                .dataSource(properties.getUrl(), properties.getUser(), properties.getPassword())
                .baselineOnMigrate(properties.isBaselineOnMigrate())
                .load();

        if (repairRequired) {
            flyway.repair();
        }

        return flyway;
    }
}

