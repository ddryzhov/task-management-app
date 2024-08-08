package mate.academy.taskmanagement.mapper;

import java.util.List;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagement.dto.label.LabelResponseDto;
import mate.academy.taskmanagement.model.Label;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    LabelResponseDto toDto(Label label);

    Label toEntity(CreateLabelRequestDto dto);

    List<LabelResponseDto> toDtoList(List<Label> labels);
}
