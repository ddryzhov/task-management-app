package mate.academy.taskmanagement.dto.project;

import java.time.LocalDate;
import java.util.Set;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.model.Project;

public record ProjectResponseDto(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Set<TaskResponseDto> tasks,
        Project.Status status
) {}
