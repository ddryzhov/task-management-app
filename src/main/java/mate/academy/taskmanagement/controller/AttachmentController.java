package mate.academy.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.attachment.AttachmentResponseDto;
import mate.academy.taskmanagement.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Operation(summary = "Upload an attachment to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment uploaded",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttachmentResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public AttachmentResponseDto uploadAttachment(
            @Parameter(description = "File to upload")
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "ID of the task to which the attachment is associated")
            @RequestParam("taskId") @NotNull Long taskId,
            Authentication authentication
    ) throws IOException {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return attachmentService.uploadAttachment(file, taskId, email, role);
    }

    @Operation(summary = "Retrieve attachments by task ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttachmentResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<AttachmentResponseDto> getAttachmentsByTaskId(
            @Parameter(description = "ID of the task to retrieve attachments for")
            @RequestParam("taskId") @NotNull Long taskId,
            Authentication authentication,
            Pageable pageable
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return attachmentService.getAttachmentsByTaskId(taskId, email, role, pageable);
    }

    @Operation(summary = "Download an attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment downloaded",
                    content = { @Content(mediaType = "application/octet-stream") }),
            @ApiResponse(responseCode = "400", description = "Invalid file ID",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/download")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Resource downloadAttachment(
            @Parameter(description = "ID of the file to download")
            @RequestParam("fileId") @NotNull String fileId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return attachmentService.downloadAttachment(fileId, email, role);
    }

    @Operation(summary = "Delete an attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid attachment ID",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{attachmentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void deleteAttachment(
            @Parameter(description = "ID of the attachment to delete")
            @PathVariable Long attachmentId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        attachmentService.deleteAttachment(attachmentId, email, role);
    }
}
