package mate.academy.taskmanagement.service;

import java.util.List;
import mate.academy.taskmanagement.dto.comment.CommentResponseDto;
import mate.academy.taskmanagement.dto.comment.CreateCommentRequestDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentResponseDto addComment(CreateCommentRequestDto requestDto, String email, String role);

    List<CommentResponseDto> getCommentsByTaskId(Long taskId, String email, String role,
                                                 Pageable pageable);
}
