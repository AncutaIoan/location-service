package angrymiaucino.locationservice.service;

import angrymiaucino.locationservice.common.dto.CreateUserRequest;
import angrymiaucino.locationservice.common.dto.NearbyUsersRequest;
import angrymiaucino.locationservice.config.cache.BloomFilterName;
import angrymiaucino.locationservice.config.cache.BloomFilterService;
import angrymiaucino.locationservice.repository.UserRepository;
import angrymiaucino.locationservice.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock();
    private final PasswordEncoder passwordEncoder = mock();
    private final BloomFilterService bloomFilterService = mock();

    private final UserService userService = new UserService(userRepository, passwordEncoder, bloomFilterService);


    @Test
    void getAllUsers_shouldReturnUserDTOs() {
        var user = new User("john", "john@example.com", "hashed");
        when(userRepository.findAll()).thenReturn(Flux.just(user));

        var result = userService.getAllUsers().collectList().block();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst().username()).isEqualTo("john");
    }

    @Test
    void getUserById_shouldReturnUserDTO() {
        var user = new User("john", "john@example.com", "hashed");
        when(userRepository.findById(1L)).thenReturn(Mono.just(user));

        var result = userService.getUserById(1L).block();

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("john");
    }

    @Test
    void getUserByUsername_shouldReturnUserDTO() {
        var user = new User("john", "john@example.com", "hashed");
        when(userRepository.findByUsername("john")).thenReturn(Mono.just(user));

        var result = userService.getUserByUsername("john").block();

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("john");
    }

    @Test
    void saveUser_shouldEncodePasswordAndSave() {
        var request = new CreateUserRequest("newUser", "new@example.com", "password", 12.34, 56.78);
        var user = new User("john", "john@example.com", "hashed-password");

        when(passwordEncoder.encode("password")).thenReturn("hashed-password");
        when(userRepository.saveWithLocation(any(User.class))).thenReturn(Mono.just(user));

        var result = userService.saveUser(request).block();

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("john");
        assertThat(result.email()).isEqualTo("john@example.com");
    }

    @Test
    void findNearbyUsers_shouldReturnUserDTOs() {
        var user = new User("john", "john@example.com", "hashed-password");
        var request = new NearbyUsersRequest(0.0, 0.0, 1000.0);

        when(userRepository.findNearbyUsers(0.0, 0.0, 1000.0)).thenReturn(Flux.just(user));

        var result = userService.findNearbyUsers(request).collectList().block();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.getFirst().username()).isEqualTo("john");
    }

    @Test
    void deleteUser_shouldCompleteSuccessfully() {
        when(userRepository.deleteById(10L)).thenReturn(Mono.empty());

        var result = userService.deleteUser(10L).block();

        assertThat(result).isNull(); // Mono<Void>.block() returns null
        verify(userRepository).deleteById(10L);
    }

    @Test
    void doesUsernameExist_whenBloomFilterHit_shouldCheckDatabase() {
        when(bloomFilterService.check("john", BloomFilterName.USER_NAME)).thenReturn(Mono.just(true));
        when(userRepository.existsByUsername("john")).thenReturn(Mono.just(true));

        var result = userService.doesUsernameExist("john").block();

        assertThat(result).isTrue();
        verify(userRepository).existsByUsername("john");
    }

    @Test
    void doesUsernameExist_whenBloomFilterMiss_shouldReturnFalseWithoutDbCheck() {
        when(bloomFilterService.check("ghost", BloomFilterName.USER_NAME)).thenReturn(Mono.just(false));

        var result = userService.doesUsernameExist("ghost").block();

        assertThat(result).isFalse();
        verify(userRepository, never()).existsByUsername(anyString());
    }
}
