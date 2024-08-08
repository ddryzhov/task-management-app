package mate.academy.taskmanagement.dto.user;

import java.util.Set;

public record UserProfileResponseDto(
        Long id,
        String login,
        String email,
        String firstName,
        String lastName,
        Set<String> roles
) {}
