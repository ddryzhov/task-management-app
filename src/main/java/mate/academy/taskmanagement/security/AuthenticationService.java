package mate.academy.taskmanagement.security;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.user.UserLoginRequestDto;
import mate.academy.taskmanagement.dto.user.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateJwtToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
