package mate.academy.taskmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.comment.CommentResponseDto;
import mate.academy.taskmanagement.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagement.exception.TaskNotFoundException;
import mate.academy.taskmanagement.exception.UserNotFoundException;
import mate.academy.taskmanagement.mapper.CommentMapper;
import mate.academy.taskmanagement.model.Comment;
import mate.academy.taskmanagement.model.Task;
import mate.academy.taskmanagement.model.User;
import mate.academy.taskmanagement.repository.CommentRepository;
import mate.academy.taskmanagement.repository.TaskRepository;
import mate.academy.taskmanagement.repository.UserRepository;
import mate.academy.taskmanagement.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto addComment(CreateCommentRequestDto requestDto,
                                         String email, String role) {
        Task task = taskRepository.findById(requestDto.taskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + requestDto.taskId() + "' not found."));

        checkTaskAccess(task, email, role);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '"
                        + email + "' not found."));

        Comment comment = commentMapper.toEntity(requestDto);
        comment.setTask(task);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByTaskId(Long taskId, String email,
                                                        String role, Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + taskId + "' not found."));

        checkTaskAccess(task, email, role);

        List<Comment> comments = commentRepository.findByTaskId(taskId, pageable);
        return commentMapper.toDtoList(comments);
    }

    private void checkTaskAccess(Task task, String email, String role) {
        if (!task.getProject().getUser().getEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission for this action.");
        }
    }
}
