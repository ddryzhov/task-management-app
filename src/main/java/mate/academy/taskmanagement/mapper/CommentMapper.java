package mate.academy.taskmanagement.mapper;

import java.util.List;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.comment.CommentResponseDto;
import mate.academy.taskmanagement.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagement.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment toEntity(CreateCommentRequestDto dto);

    @Mapping(source = "task.id", target = "taskId")
    CommentResponseDto toDto(Comment comment);

    List<CommentResponseDto> toDtoList(List<Comment> comments);
}
