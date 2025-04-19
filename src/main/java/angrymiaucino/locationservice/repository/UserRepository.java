package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);
    @Query("""
        SELECT * FROM users 
        WHERE ST_DWithin(
            location, 
            ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, 
            :radius
        )
    """)
    Flux<User> findNearbyUsers(double latitude, double longitude, double radius);

    @Query("""
        INSERT INTO users (username, email, password, latitude, longitude, location)
        VALUES (:#{#user.username}, :#{#user.email}, :#{#user.password}, :#{#user.latitude}, :#{#user.longitude},
            ST_SetSRID(ST_MakePoint(:#{#user.longitude}, :#{#user.latitude}), 4326)::geography)
        RETURNING *
    """)
    Mono<User> saveWithLocation(@Param("user") User user);

}
