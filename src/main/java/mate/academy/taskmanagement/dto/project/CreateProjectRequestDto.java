package mate.academy.taskmanagement.dto.project;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.taskmanagement.model.Project;

@Data
@Accessors(chain = true)
public class CreateProjectRequestDto {
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Project.Status status;
}
