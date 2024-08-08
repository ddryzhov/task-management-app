package mate.academy.taskmanagement.repository;

import java.util.List;
import mate.academy.taskmanagement.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query("SELECT b FROM Task b JOIN b.project c WHERE c.id = :projectId")
    List<Task> findByProjectId(Long projectId, Pageable pageable);

    boolean existsByName(String name);
}
