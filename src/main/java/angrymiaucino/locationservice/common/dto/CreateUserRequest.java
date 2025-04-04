package angrymiaucino.locationservice.common.dto;

public record CreateUserRequest(String username, String email, String password) {
    // This record will contain the necessary fields for creating a user
}
