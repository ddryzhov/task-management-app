package mate.academy.taskmanagement.mapper;

import java.util.List;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {ProjectMapper.class, UserMapper.class})
public interface TaskMapper {
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    Task toEntity(CreateTaskRequestDto requestDto);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskResponseDto toDto(Task task);

    List<TaskResponseDto> toDtoList(List<Task> tasks);
}
