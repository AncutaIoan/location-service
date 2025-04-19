package angrymiaucino.locationservice.controller.graphql;

import angrymiaucino.locationservice.common.dto.NearbyUsersRequest;
import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class UserGraphQLController {
    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public Flux<UserDTO> nearbyUsers(
            @Argument double latitude,
            @Argument double longitude,
            @Argument double radius
    ) {
        return userService
                .findNearbyUsers(new NearbyUsersRequest(latitude, longitude, radius));
    }
}
