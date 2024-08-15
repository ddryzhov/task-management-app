package mate.academy.taskmanagement.dto.label;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateLabelRequestDto {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;
}
