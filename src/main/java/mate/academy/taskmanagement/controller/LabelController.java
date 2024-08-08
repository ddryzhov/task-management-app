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
import mate.academy.taskmanagement.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagement.dto.label.LabelResponseDto;
import mate.academy.taskmanagement.dto.label.UpdateLabelRequestDto;
import mate.academy.taskmanagement.service.LabelService;
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
@RequestMapping("/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @Operation(summary = "Create a new label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public LabelResponseDto createLabel(
            @Parameter(description = "Label details")
            @Valid @RequestBody CreateLabelRequestDto requestDto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return labelService.createLabel(requestDto, email, role);
    }

    @Operation(summary = "Update an existing label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Label not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public LabelResponseDto updateLabel(
            @Parameter(description = "ID of the label to update")
            @PathVariable Long id,
            @Parameter(description = "Updated label details")
            @Valid @RequestBody UpdateLabelRequestDto requestDto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return labelService.updateLabel(id, requestDto, email, role);
    }

    @Operation(summary = "Retrieve a label by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Label not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public LabelResponseDto getLabelById(
            @Parameter(description = "ID of the label to retrieve")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return labelService.getLabelById(id, email, role);
    }

    @Operation(summary = "Retrieve all labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Labels retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabelResponseDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<LabelResponseDto> getAllLabels(Authentication authentication) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return labelService.getAllLabels(email, role);
    }

    @Operation(summary = "Delete a label by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Label deleted",
                    content = { @Content }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Label not found",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void deleteLabel(
            @Parameter(description = "ID of the label to delete")
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        labelService.deleteLabel(id, email, role);
    }
}
