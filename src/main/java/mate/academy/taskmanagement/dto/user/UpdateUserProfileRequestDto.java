package mate.academy.taskmanagement.dto.user;

import jakarta.persistence.Column;

public record UpdateUserProfileRequestDto(
        @Column(nullable = false)
        String login,

        @Column(nullable = false)
        String firstName,

        @Column(nullable = false)
        String lastName
) {}
