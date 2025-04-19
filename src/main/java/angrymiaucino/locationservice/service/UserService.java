package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.common.dto.CreateUserRequest;
import angrymiaucino.locationservice.common.dto.NearbyUsersRequest;
import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.config.cache.BloomFilterName;
import angrymiaucino.locationservice.config.cache.BloomFilterService;
import angrymiaucino.locationservice.repository.UserRepository;
import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BloomFilterService bloomFilterService;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, BloomFilterService bloomFilterService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bloomFilterService = bloomFilterService;
    }

    public Flux<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    public Mono<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    public Mono<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()));
    }

    public Mono<UserDTO> saveUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest, passwordEncoder.encode(createUserRequest.password()));

        return userRepository.saveWithLocation(user)
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()));
    }

    public Flux<UserDTO> findNearbyUsers(NearbyUsersRequest nearbyUsersRequest) {
        return userRepository.findNearbyUsers(nearbyUsersRequest.latitude(), nearbyUsersRequest.longitude(), nearbyUsersRequest.radius())
                   .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()));
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<Boolean> doesUsernameExist(String username) {
        return bloomFilterService.check(username, BloomFilterName.USER_NAME)
                .flatMap(hit -> handle(username, hit));
    }

    private Mono<Boolean> handle(String username, Boolean hit) {
        if (hit) {
            return userRepository.existsByUsername(username);
        } else {
            return Mono.just(false);
        }
    }
}
