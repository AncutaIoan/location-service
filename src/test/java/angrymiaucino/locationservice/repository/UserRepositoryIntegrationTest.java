package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.config.AbstractTestcontainersIntegrationTest;
import angrymiaucino.locationservice.config.ComponentTest;
import angrymiaucino.locationservice.config.RunSql;
import angrymiaucino.locationservice.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
public class UserRepositoryIntegrationTest extends AbstractTestcontainersIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @RunSql(scripts = { "/sql/users.sql" })
    void testFindByUsername() {
        User user = userRepository.findByUsername("far_user").block();

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("far_user");
        assertThat(user.getEmail()).isEqualTo("far@example.com");
    }

    @Test
    @RunSql(scripts = { "/sql/users.sql" })
    void testFindNearbyUsers() {
        List<User> users = userRepository.findNearbyUsers(40.7128, -74.0060, 100).collectList().block();

        assertThat(users).hasSize(1);
    }
}
