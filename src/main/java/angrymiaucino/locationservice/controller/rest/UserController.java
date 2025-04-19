package angrymiaucino.locationservice.controller.rest;

import angrymiaucino.locationservice.common.dto.CreateUserRequest;
import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public Mono<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public Mono<UserDTO> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.saveUser(createUserRequest);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/exists/{username}")
    public Mono<Boolean> existsUser(@PathVariable String username) {
        return userService.doesUsernameExist(username);
    }
}
