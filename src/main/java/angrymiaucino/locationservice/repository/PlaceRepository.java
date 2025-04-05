package angrymiaucino.locationservice.repository;

import angrymiaucino.locationservice.repository.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // Add custom queries here if needed
}
