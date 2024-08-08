package mate.academy.taskmanagement.repository;

import java.util.List;
import mate.academy.taskmanagement.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId, Pageable pageable);
}
