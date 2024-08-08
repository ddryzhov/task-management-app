package mate.academy.taskmanagement.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.taskmanagement.model.Attachment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTaskId(Long taskId, Pageable pageable);

    Optional<Attachment> findByDropboxFileId(String dropboxFileId);
}
