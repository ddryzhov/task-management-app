package mate.academy.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagement.dto.task.TaskResponseDto;
import mate.academy.taskmanagement.dto.task.UpdateTaskRequestDto;
import mate.academy.taskmanagement.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public TaskResponseDto createTask(
            @Parameter(description = "Task details to create")
            @Valid @RequestBody CreateTaskRequestDto requestDto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return taskService.createTask(requestDto, email, role);
    }

    @Operation(summary = "Retrieve tasks for a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<TaskResponseDto> getTasksForProject(
            @Parameter(description = "ID of the project to retrieve tasks from")
            @RequestParam Long projectId,
            Authentication authentication,
            @Parameter(description = "Pagination information")
            Pageable pageable
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return taskService.getTasksForProject(projectId, email, role, pageable);
    }

    @Operation(summary = "Retrieve a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public TaskResponseDto getTaskById(
            @Parameter(description = "ID of the task to retrieve")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return taskService.getTaskById(id, email, role);
    }

    @Operation(summary = "Update an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public TaskResponseDto updateTask(
            @Parameter(description = "ID of the task to update")
            @PathVariable Long id,
            @Parameter(description = "Updated task details")
            @Valid @RequestBody UpdateTaskRequestDto requestDto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return taskService.updateTask(id, requestDto, email, role);
    }

    @Operation(summary = "Delete a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted",
                    content = { @Content }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void deleteTask(
            @Parameter(description = "ID of the task to delete")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        taskService.deleteTask(id, email, role);
    }
}
