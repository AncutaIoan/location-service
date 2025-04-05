package angrymiaucino.locationservice.controller.graphql;

import angrymiaucino.locationservice.repository.entity.Place;
import angrymiaucino.locationservice.service.PlaceService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PlaceGraphQLController {

    private final PlaceService placeService;

    public PlaceGraphQLController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @QueryMapping
    public Place placeById(@Argument Long id) {
        return placeService.findById(id);
    }

    @QueryMapping
    public List<Place> allPlaces() {
        return placeService.findAll();
    }

    @MutationMapping
    public Place createPlace(
            @Argument String name,
            @Argument String description,
            @Argument Double latitude,
            @Argument Double longitude
    ) {
        return placeService.createPlace(name, description, latitude, longitude);
    }
}
