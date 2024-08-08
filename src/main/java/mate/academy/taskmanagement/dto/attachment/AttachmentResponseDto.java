package mate.academy.taskmanagement.dto.attachment;

import java.time.LocalDateTime;

public record AttachmentResponseDto(
        Long id,
        Long taskId,
        String dropboxFileId,
        String filename,
        LocalDateTime uploadDate
) {}
