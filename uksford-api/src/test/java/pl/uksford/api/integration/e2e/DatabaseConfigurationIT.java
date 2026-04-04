package pl.uksford.api.integration.e2e;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.uksford.api.configuration.TestcontainersInitializer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class DatabaseConfigurationIT {
    @Autowired
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Test
    @DisplayName("Should check whether connection with database was successfully obtained.")
    void shouldTestConnectionWithDatabase() {
        try (Connection connection = ds.getConnection()
        ) {
            // given, when, then
            Assertions.assertFalse(connection.isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should query database enum to check proper initialization of a schema")
    void whenQueryDatabase_shouldProperlyFetchAcademicDegrees() {
        // given
        // when
        List<String> actual = new ArrayList<>(em.createNativeQuery(
                "select * from unnest(enum_range(NULL::academic_degree))"
        ).getResultList());
        // then
        List<String> expected = getListOfAcademicDegrees();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    public static List<String> getListOfAcademicDegrees() {
        return List.of("mgr.", "dok.", "dok. hab.", "prof. uczelni", "prof.", "mgr. inż", "dok. inż", "dok. hab. inż.");}
}