package mate.academy.taskmanagement.dto.comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        Long taskId,
        String text,
        LocalDateTime timestamp
) {}
