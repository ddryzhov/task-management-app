package mate.academy.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.user.UserLoginRequestDto;
import mate.academy.taskmanagement.dto.user.UserLoginResponseDto;
import mate.academy.taskmanagement.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagement.dto.user.UserResponseDto;
import mate.academy.taskmanagement.exception.RegistrationException;
import mate.academy.taskmanagement.mapper.UserMapper;
import mate.academy.taskmanagement.model.User;
import mate.academy.taskmanagement.security.AuthenticationService;
import mate.academy.taskmanagement.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user registration and login")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user",
            description = "Endpoint to register a new user into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content)
    })
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(
            @Parameter(description = "User registration request data")
            @Valid @RequestBody UserRegistrationRequestDto request
    ) throws RegistrationException {
        User user = userService.register(request);
        return userMapper.toDto(user);
    }

    @Operation(summary = "User login",
            description = "Endpoint for user login, returns authentication token if successful.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserLoginResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content)
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @Parameter(description = "User login request data")
            @Valid @RequestBody UserLoginRequestDto requestDto
    ) {
        return authenticationService.authenticate(requestDto);
    }
}
