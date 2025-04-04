package angrymiaucino.locationservice.common.dto;

public record UserDTO(Long id, String username, String email) {
    // No need to manually create constructors, getters, or setters,
    // as records automatically generate them.
}
