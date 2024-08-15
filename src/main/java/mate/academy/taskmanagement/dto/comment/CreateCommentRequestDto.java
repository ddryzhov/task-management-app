package mate.academy.taskmanagement.dto.comment;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto(
        @NotNull
        Long taskId,

        @Column(nullable = false)
        String text
) {}
