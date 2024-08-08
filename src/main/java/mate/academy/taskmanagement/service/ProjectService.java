package mate.academy.taskmanagement.service;

import java.util.List;
import mate.academy.taskmanagement.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagement.dto.project.ProjectResponseDto;
import mate.academy.taskmanagement.dto.project.UpdateProjectRequestDto;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectResponseDto createProject(CreateProjectRequestDto requestDto);

    ProjectResponseDto updateProject(Long id, UpdateProjectRequestDto requestDto);

    ProjectResponseDto getProjectById(Long id, String email, String role);

    List<ProjectResponseDto> getUserProjects(String email, String role, Pageable pageable);

    void deleteProject(Long id, String email, String role);
}
