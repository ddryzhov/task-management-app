package mate.academy.taskmanagement.service;

import java.io.IOException;
import java.util.List;
import mate.academy.taskmanagement.dto.attachment.AttachmentResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    AttachmentResponseDto uploadAttachment(MultipartFile file, Long taskId,
                                           String email, String role) throws IOException;

    List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId, String email, String role,
                                                       Pageable pageable);

    Resource downloadAttachment(String fileId, String email, String role);

    void deleteAttachment(Long attachmentId, String email, String role);
}
