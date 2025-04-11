package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    // You can add custom query methods here if needed, e.g. findByUsername or findByEmail
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
}
