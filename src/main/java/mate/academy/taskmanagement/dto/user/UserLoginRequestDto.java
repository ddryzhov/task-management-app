package mate.academy.taskmanagement.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @Column(nullable = false)
        @Length(min = 8, max = 40)
        @Email
        String email,

        @Column(nullable = false)
        @Length(min = 8, max = 20)
        String password
) {}
