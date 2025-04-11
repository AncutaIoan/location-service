package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.Place;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends R2dbcRepository<Place, Long> {
    // Add custom queries here if needed
}
