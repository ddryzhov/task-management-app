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
import mate.academy.taskmanagement.dto.comment.CommentResponseDto;
import mate.academy.taskmanagement.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagement.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Add a new comment to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public CommentResponseDto addComment(
            @Parameter(description = "Comment details")
            @Valid @RequestBody CreateCommentRequestDto requestDto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return commentService.addComment(requestDto, email, role);
    }

    @Operation(summary = "Retrieve comments for a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid task ID",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public List<CommentResponseDto> getCommentsByTaskId(
            @Parameter(description = "ID of the task to retrieve comments for")
            @RequestParam Long taskId,
            Authentication authentication,
            Pageable pageable
    ) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return commentService.getCommentsByTaskId(taskId, email, role, pageable);
    }
}
