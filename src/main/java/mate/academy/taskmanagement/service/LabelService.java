package mate.academy.taskmanagement.service;

import java.util.List;
import mate.academy.taskmanagement.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagement.dto.label.LabelResponseDto;
import mate.academy.taskmanagement.dto.label.UpdateLabelRequestDto;

public interface LabelService {
    LabelResponseDto createLabel(CreateLabelRequestDto labelDto, String email, String role);

    LabelResponseDto updateLabel(Long id, UpdateLabelRequestDto requestDto,
                                 String email, String role);

    LabelResponseDto getLabelById(Long id, String email, String role);

    List<LabelResponseDto> getAllLabels(String email, String role);

    void deleteLabel(Long id, String email, String role);
}
