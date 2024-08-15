package mate.academy.taskmanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagement.dto.label.LabelResponseDto;
import mate.academy.taskmanagement.dto.label.UpdateLabelRequestDto;
import mate.academy.taskmanagement.exception.LabelNotFoundException;
import mate.academy.taskmanagement.exception.TaskNotFoundException;
import mate.academy.taskmanagement.mapper.LabelMapper;
import mate.academy.taskmanagement.model.Label;
import mate.academy.taskmanagement.model.Task;
import mate.academy.taskmanagement.repository.LabelRepository;
import mate.academy.taskmanagement.repository.TaskRepository;
import mate.academy.taskmanagement.service.LabelService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;
    private final LabelMapper labelMapper;

    @Override
    @Transactional
    public LabelResponseDto createLabel(CreateLabelRequestDto requestDto,
                                        String email, String role) {
        Label label = labelMapper.toEntity(requestDto);
        Task task = taskRepository.findById(requestDto.taskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + requestDto.taskId() + "' not found."));
        checkTaskAccess(task, email, role);
        label.getTasks().add(task);
        task.getLabels().add(label);

        label = labelRepository.save(label);
        return labelMapper.toDto(label);
    }

    @Override
    @Transactional
    public LabelResponseDto updateLabel(Long id, UpdateLabelRequestDto requestDto,
                                        String email, String role) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label with ID '"
                        + id + "' not found."));

        label.getTasks().forEach(task -> checkTaskAccess(task, email, role));

        label.setName(requestDto.getName());
        label.setColor(requestDto.getColor());
        label = labelRepository.save(label);
        return labelMapper.toDto(label);
    }

    @Override
    public LabelResponseDto getLabelById(Long id, String email, String role) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label with ID '"
                        + id + "' not found."));

        label.getTasks().forEach(task -> checkTaskAccess(task, email, role));
        return labelMapper.toDto(label);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelResponseDto> getAllLabels(String email, String role) {
        List<Label> labels;
        if (role.equals("ROLE_ADMIN")) {
            labels = labelRepository.findAll();
        } else {
            labels = labelRepository.findAll().stream()
                    .filter(label -> label.getTasks().stream()
                            .anyMatch(task -> task.getProject().getUser().getEmail().equals(email)))
                    .collect(Collectors.toList());
        }
        return labelMapper.toDtoList(labels);
    }

    @Override
    @Transactional
    public void deleteLabel(Long id, String email, String role) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label with ID '"
                        + id + "' not found."));

        label.getTasks().forEach(task -> checkTaskAccess(task, email, role));
        labelRepository.delete(label);
    }

    private void checkTaskAccess(Task task, String email, String role) {
        if (!task.getProject().getUser().getEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission for this action.");
        }
    }
}
