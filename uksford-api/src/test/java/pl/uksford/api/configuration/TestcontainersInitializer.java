package pl.uksford.api.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.io.IOException;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine")
            .withDatabaseName("uksford_db")
            .withUsername("dev_user")
            .withPassword("dev_password")
            .withReuse(true);

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        if (!postgres.isRunning()) {
            postgres.start();
        }

        // to enable use of psql it is required to execute sql file with execInContainer method to pass necessary arguments
        try {
            postgres.execInContainer(
                    "psql",
                    "-U", "dev_user",
                    "-d", "uksford_db",
                    "-f", "/docker-entrypoint-initdb.d/user-init.sql"
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // save properties set for container to context environment
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
        ).applyTo(ctx.getEnvironment());

        // configure flyway and run migrations manually for container
        Flyway flyway = Flyway.configure()
                .dataSource(postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword())
                .load();
        flyway.migrate();
    }
}
