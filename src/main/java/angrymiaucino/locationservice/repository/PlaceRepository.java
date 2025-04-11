package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.Place;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlaceRepository extends R2dbcRepository<Place, Long> {

    // Save place with geolocation
    @Query("""
           INSERT INTO place (name, description, latitude, longitude, location)
           VALUES (:#{#place.name}, :#{#place.description}, :#{#place.latitude}, :#{#place.longitude},
           ST_SetSRID(ST_MakePoint(:#{#place.longitude}, :#{#place.latitude}), 4326))
           RETURNING id, name, description, latitude, longitude, location
           """)
    Mono<Place> savePlaceWithGeolocation(Place place);

    // Update place with geolocation
    @Query("""
           UPDATE place SET name = :#{#place.name}, description = :#{#place.description},
           latitude = :#{#place.latitude}, longitude = :#{#place.longitude},
           location = ST_SetSRID(ST_MakePoint(:#{#place.longitude}, :#{#place.latitude}), 4326)
           WHERE id = :#{#place.id}
           RETURNING id, name, description, latitude, longitude, location
           """)
    Mono<Place> updatePlaceWithGeolocation(Place place);
}