package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.common.dto.UserDTO;
import angrymiaucino.locationservice.repository.UserRepository;
import angrymiaucino.locationservice.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
