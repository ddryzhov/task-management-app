package mate.academy.taskmanagement.dto.user;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateUserRoleRequestDto {
    @Column(nullable = false)
    private String roleName;
}
