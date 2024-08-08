package mate.academy.taskmanagement.dto.task;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import mate.academy.taskmanagement.model.Task;

@Getter
@Setter
public class UpdateTaskRequestDto {
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    private Task.Priority priority;

    @NotNull
    private Task.Status status;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private Long assigneeId;
}
