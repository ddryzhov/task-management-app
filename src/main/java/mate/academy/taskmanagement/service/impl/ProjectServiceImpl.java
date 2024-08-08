package mate.academy.taskmanagement.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagement.dto.project.ProjectResponseDto;
import mate.academy.taskmanagement.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagement.exception.ProjectNotFoundException;
import mate.academy.taskmanagement.exception.UserNotFoundException;
import mate.academy.taskmanagement.mapper.ProjectMapper;
import mate.academy.taskmanagement.model.Project;
import mate.academy.taskmanagement.model.User;
import mate.academy.taskmanagement.repository.ProjectRepository;
import mate.academy.taskmanagement.repository.UserRepository;
import mate.academy.taskmanagement.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectResponseDto createProject(CreateProjectRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email '" + userEmail
                        + "' not found."));

        Project project = projectMapper.toEntity(requestDto);
        project.setUser(user);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(Long id, UpdateProjectRequestDto requestDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID '" + id
                        + "' not found."));
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setStatus(requestDto.getStatus());
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectResponseDto getProjectById(Long id, String email, String role) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID '"
                        + id + "' not found."));
        checkProjectAccess(project, email, role);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getUserProjects(String email, String role, Pageable pageable) {
        if (role.equals("ROLE_ADMIN")) {
            return projectMapper.toDtoList(projectRepository.findAll());
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email '"
                        + email + "' not found."));
        List<Project> projects = projectRepository.findByUserId(user.getId(), pageable);
        return projectMapper.toDtoList(projects);
    }

    @Override
    @Transactional
    public void deleteProject(Long id, String email, String role) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID '"
                        + id + "' not found."));
        checkProjectAccess(project, email, role);
        projectRepository.delete(project);
    }

    private void checkProjectAccess(Project project, String email, String role) {
        if (!project.getUser().getEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission for this action.");
        }
    }
}
