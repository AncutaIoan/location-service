package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Cacheable(value = "${infinispan.embedded.cache.locationCache}", key = "'allPlaces'")
    public Flux<Place> findAll() {
        return placeRepository.findAll();
    }

    @Cacheable(value = "${infinispan.embedded.cache.locationCache}", key = "#id")
    public Mono<Place> findById(Long id) {
        return placeRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Place with id " + id + " not found")));
    }

    public Mono<Place> createPlace(String name, String description, Double lat, Double lng) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(new Coordinate(lat, lng));

        Place place = new Place(name, description, lat, lng, location);
        return placeRepository.save(place);
    }
}
