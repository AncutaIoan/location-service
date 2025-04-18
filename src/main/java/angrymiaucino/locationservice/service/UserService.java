package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.common.dto.CreateUserRequest;
import angrymiaucino.locationservice.common.dto.UserDTO;
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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        return userRepository.save(user)
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()));
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}
