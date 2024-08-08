package mate.academy.taskmanagement.service;

import java.util.List;
import mate.academy.taskmanagement.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.dto.task.UpdateTaskRequestDto;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponseDto createTask(CreateTaskRequestDto requestDto, String email, String role);

    TaskResponseDto updateTask(Long id, UpdateTaskRequestDto requestDto, String email, String role);

    TaskResponseDto getTaskById(Long id, String email, String role);

    List<TaskResponseDto> getTasksForProject(Long projectId, String email, String role,
                                             Pageable pageable);

    void deleteTask(Long id, String email, String role);
}
