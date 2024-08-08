package mate.academy.taskmanagement.dto.user;

public record UserResponseDto(
        Long id,
        String login,
        String email,
        String firstName,
        String lastName
) {}

