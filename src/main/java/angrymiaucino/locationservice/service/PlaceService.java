package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaceService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Cacheable(value = "#{T(angrymiaucino.locationservice.config.cache.CacheName).LOCATION_CACHE.name()}", key = "'all'")
    public Flux<Place> findAll() {
        logger.debug("Cache miss for all places");

        return placeRepository.findAll();
    }

    @Cacheable(value = "#{T(angrymiaucino.locationservice.config.cache.CacheName).LOCATION_CACHE.name()}", key = "#id")
    public Mono<Place> findById(Long id) {
        logger.debug("Cache miss for place with id: {}", id);

        return placeRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Place with id " + id + " not found")));
    }

    public Mono<Place> createPlace(String name, String description, Double lat, Double lng) {

        Place place = new Place(name, description, lat, lng);
        return placeRepository.savePlaceWithGeolocation(place);
    }
}
