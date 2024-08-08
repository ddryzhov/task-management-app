package mate.academy.taskmanagement.mapper;

import java.util.List;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagement.dto.project.ProjectResponseDto;
import mate.academy.taskmanagement.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {TaskMapper.class, UserMapper.class})
public interface ProjectMapper {
    @Mapping(target = "id", ignore = true)
    Project toEntity(CreateProjectRequestDto dto);

    ProjectResponseDto toDto(Project project);

    List<ProjectResponseDto> toDtoList(List<Project> projects);
}
