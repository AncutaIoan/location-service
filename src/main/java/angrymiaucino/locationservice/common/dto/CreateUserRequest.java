package angrymiaucino.locationservice.common.dto;

public record CreateUserRequest(String username, String email, String password, Double latitude, Double longitude) {
    // This record will contain the necessary fields for creating a user
}
