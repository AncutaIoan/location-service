package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.common.dto.CreateUserRequest;
import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.repository.UserRepository;
import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        }
        return null;
    }

    public UserDTO saveUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.username(), createUserRequest.email(), passwordEncoder.encode(createUserRequest.password()));

        User savedUser =  userRepository.save(user);

        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
