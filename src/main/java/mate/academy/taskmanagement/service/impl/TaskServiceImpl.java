package mate.academy.taskmanagement.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.dto.task.UpdateTaskRequestDto;
import mate.academy.taskmanagement.exception.ProjectNotFoundException;
import mate.academy.taskmanagement.exception.TaskNameAlreadyExistsException;
import mate.academy.taskmanagement.exception.TaskNotFoundException;
import mate.academy.taskmanagement.exception.UserNotFoundException;
import mate.academy.taskmanagement.mapper.TaskMapper;
import mate.academy.taskmanagement.model.Project;
import mate.academy.taskmanagement.model.Task;
import mate.academy.taskmanagement.model.User;
import mate.academy.taskmanagement.repository.ProjectRepository;
import mate.academy.taskmanagement.repository.TaskRepository;
import mate.academy.taskmanagement.repository.UserRepository;
import mate.academy.taskmanagement.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto requestDto, String email, String role) {
        if (taskRepository.existsByName(requestDto.getName())) {
            throw new TaskNameAlreadyExistsException("Task with the name '"
                    + requestDto.getName() + "' already exists.");
        }

        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID '"
                        + requestDto.getProjectId() + "' not found."));

        checkProjectAccess(project, email, role);

        User assignee = userRepository.findById(requestDto.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException("User with ID '"
                        + requestDto.getAssigneeId() + "' not found."));

        if (!project.getUser().getEmail().equals(assignee.getEmail())
                && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission "
                    + "to assign tasks to this user.");
        }

        Task task = taskMapper.toEntity(requestDto);
        task.setProject(project);
        task.setAssignee(assignee);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long id, UpdateTaskRequestDto requestDto,
                                      String email, String role) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + id + "' not found."));

        checkProjectAccess(task.getProject(), email, role);

        task.setName(requestDto.getName());
        task.setDescription(requestDto.getDescription());
        task.setPriority(requestDto.getPriority());
        task.setStatus(requestDto.getStatus());
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponseDto getTaskById(Long id, String email, String role) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + id + "' not found."));

        checkProjectAccess(task.getProject(), email, role);
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksForProject(Long projectId, String email,
                                                    String role, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID '"
                        + projectId + "' not found."));

        checkProjectAccess(project, email, role);
        List<Task> tasks = taskRepository.findByProjectId(projectId, pageable);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    @Transactional
    public void deleteTask(Long id, String email, String role) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + id + "' not found."));

        checkProjectAccess(task.getProject(), email, role);
        taskRepository.delete(task);
    }

    private void checkProjectAccess(Project project, String email, String role) {
        if (!project.getUser().getEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission for this action.");
        }
    }
}
