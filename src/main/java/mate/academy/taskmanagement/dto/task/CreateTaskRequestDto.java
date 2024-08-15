package mate.academy.taskmanagement.dto.task;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.taskmanagement.model.Task;

@Data
@Accessors(chain = true)
public class CreateTaskRequestDto {
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
    private Long projectId;

    @NotNull
    private Long assigneeId;
}
