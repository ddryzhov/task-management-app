package mate.academy.taskmanagement.dto.task;

import java.time.LocalDate;
import mate.academy.taskmanagement.model.Task;

public record TaskResponseDto(
        Long id,
        String name,
        String description,
        Task.Priority priority,
        Task.Status status,
        LocalDate dueDate,
        Long projectId,
        Long assigneeId
) {}
