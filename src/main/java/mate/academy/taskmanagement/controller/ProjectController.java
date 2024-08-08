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
import mate.academy.taskmanagement.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagement.dto.project.ProjectResponseDto;
import mate.academy.taskmanagement.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagement.service.ProjectService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ProjectResponseDto createProject(
            @Parameter(description = "Project details to create")
            @Valid @RequestBody CreateProjectRequestDto requestDto
    ) {
        return projectService.createProject(requestDto);
    }

    @Operation(summary = "Retrieve all projects for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<ProjectResponseDto> getUserProjects(
            Authentication authentication,
            @Parameter(description = "Pagination information") Pageable pageable
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return projectService.getUserProjects(email, role, pageable);
    }

    @Operation(summary = "Retrieve a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ProjectResponseDto getProjectById(
            @Parameter(description = "ID of the project to retrieve")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return projectService.getProjectById(id, email, role);
    }

    @Operation(summary = "Update an existing project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ProjectResponseDto updateProject(
            @Parameter(description = "ID of the project to update")
            @PathVariable Long id,
            @Parameter(description = "Updated project details")
            @Valid @RequestBody UpdateProjectRequestDto requestDto
    ) {
        return projectService.updateProject(id, requestDto);
    }

    @Operation(summary = "Delete a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted",
                    content = { @Content }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void deleteProject(
            @Parameter(description = "ID of the project to delete")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        projectService.deleteProject(id, email, role);
    }
}
