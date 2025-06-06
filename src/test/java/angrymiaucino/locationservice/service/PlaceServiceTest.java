package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.config.cache.RedisCacheProxy;
import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlaceServiceTest {

    private final PlaceRepository placeRepository = mock();
    private final RedisCacheProxy<Long, Place> placeRedisCache = mock();

    private final PlaceService placeService = new PlaceService(placeRepository, placeRedisCache);

    @Test
    void findAll_shouldReturnAllPlaces() {
        var place1 = new Place("Park", "Nice park", 10.0, 20.0);
        var place2 = new Place("Museum", "Art museum", 15.0, 25.0);

        when(placeRepository.findAll()).thenReturn(Flux.just(place1, place2));

        List<Place> result = placeService.findAll().collectList().block();

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting(Place::getName)
                .containsExactly("Park", "Museum");
    }

    @Test
    void findById_shouldUseCacheProxy() {
        var place = new Place("Lake", "Calm lake", 5.0, 5.0);
        when(placeRedisCache.cached(eq(1L), any())).thenReturn(Mono.just(place));

        var result = placeService.findById(1L).block();

        assertThat(result)
                .isNotNull()
                .extracting(Place::getName)
                .isEqualTo("Lake");

        verify(placeRedisCache).cached(eq(1L), any());
    }

    @Test
    void findPlacesNear_shouldDelegateToRepository() {
        var place = new Place("Cafe", "Good coffee", 10.1, 20.2);
        when(placeRepository.findPlacesNear(10.0, 20.0, 1000.0)).thenReturn(Flux.just(place));

        List<Place> result = placeService.findPlacesNear(10.0, 20.0, 1000.0).collectList().block();

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(Place::getDescription)
                .isEqualTo("Good coffee");

        verify(placeRepository).findPlacesNear(10.0, 20.0, 1000.0);
    }

    @Test
    void createPlace_shouldSaveWithGeoLocation() {
        var place = new Place("Library", "Quiet study area", 11.0, 22.0);
        when(placeRepository.savePlaceWithGeolocation(any(Place.class))).thenReturn(Mono.just(place));

        var result = placeService.createPlace("Library", "Quiet study area", 11.0, 22.0).block();

        assertThat(result)
                .isNotNull()
                .extracting(Place::getName)
                .isEqualTo("Library");

        verify(placeRepository).savePlaceWithGeolocation(any(Place.class));
    }
}
