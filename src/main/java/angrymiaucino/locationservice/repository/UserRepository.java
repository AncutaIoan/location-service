package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom query methods here if needed, e.g. findByUsername or findByEmail
    User findByUsername(String username);
    User findByEmail(String email);
}
