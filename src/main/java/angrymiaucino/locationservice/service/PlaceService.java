package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.config.cache.RedisCacheProxy;
import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaceService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    private final PlaceRepository placeRepository;
    private final RedisCacheProxy<Long, Place> placeRedisCache;


    public PlaceService(PlaceRepository placeRepository, @Qualifier("placeCache") RedisCacheProxy<Long, Place> placeRedisCache) {
        this.placeRepository = placeRepository;
        this.placeRedisCache = placeRedisCache;
    }

    public Flux<Place> findAll() {
        logger.debug("Cache miss for all places");

        return placeRepository.findAll();
    }

    public Mono<Place> findById(Long id) {
        return placeRedisCache.cached(id, () -> placeRepository.findById(id));
    }

    public Flux<Place> findPlacesNear(double latitude, double longitude, double radius) {
        return placeRepository.findPlacesNear(latitude, longitude, radius);
    }

    public Mono<Place> createPlace(String name, String description, Double lat, Double lng) {
        Place place = new Place(name, description, lat, lng);
        return placeRepository.savePlaceWithGeolocation(place);
    }
}
