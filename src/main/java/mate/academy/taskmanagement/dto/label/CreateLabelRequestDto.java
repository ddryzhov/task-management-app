package mate.academy.taskmanagement.dto.label;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record CreateLabelRequestDto(
        @Column(nullable = false)
        String name,

        String color,

        @NotNull
        Long taskId
) {}
