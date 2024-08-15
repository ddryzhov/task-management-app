package mate.academy.taskmanagement.repository;

import java.util.Optional;
import mate.academy.taskmanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(Role.RoleName roleName);
}
