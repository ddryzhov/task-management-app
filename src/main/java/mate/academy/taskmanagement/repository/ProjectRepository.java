package mate.academy.taskmanagement.repository;

import java.util.List;
import mate.academy.taskmanagement.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT b FROM Project b JOIN b.user c WHERE c.id = :userId")
    List<Project> findByUserId(Long userId, Pageable pageable);
}
