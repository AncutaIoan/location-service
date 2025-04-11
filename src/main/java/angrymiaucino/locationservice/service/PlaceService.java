package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.repository.PlaceRepository;
import angrymiaucino.locationservice.repository.entity.Place;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(new Coordinate(lat, lng));

        Place place = new Place(name, description, lat, lng, location);
        return placeRepository.save(place);
    }
}
