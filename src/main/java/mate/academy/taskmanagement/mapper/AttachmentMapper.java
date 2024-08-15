package mate.academy.taskmanagement.mapper;

import java.util.List;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.attachment.AttachmentResponseDto;
import mate.academy.taskmanagement.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {
    @Mapping(source = "task.id", target = "taskId")
    AttachmentResponseDto toDto(Attachment attachment);

    List<AttachmentResponseDto> toDtoList(List<Attachment> attachments);
}
