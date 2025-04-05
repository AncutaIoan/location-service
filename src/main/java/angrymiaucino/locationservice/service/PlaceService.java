package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Place findById(Long id) {
        return placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place with id " + id + " not found"));
    }

    public Place createPlace(String name, String description, Double lat, Double lng) {
        Place place = new Place(name, description, lat, lng);
        return placeRepository.save(place);
    }
}
