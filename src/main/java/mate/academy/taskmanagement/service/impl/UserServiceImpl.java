package mate.academy.taskmanagement.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.user.UpdateUserProfileRequestDto;
import mate.academy.taskmanagement.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagement.exception.RegistrationException;
import mate.academy.taskmanagement.exception.UserNotFoundException;
import mate.academy.taskmanagement.mapper.UserMapper;
import mate.academy.taskmanagement.model.Role;
import mate.academy.taskmanagement.model.User;
import mate.academy.taskmanagement.repository.RoleRepository;
import mate.academy.taskmanagement.repository.UserRepository;
import mate.academy.taskmanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Registration failed: Email "
                    + request.getEmail() + " is already taken.");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role defaultRole = roleRepository.findByRole(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role 'ROLE_USER' not found."));
        user.setRoles(Set.of(defaultRole));
        user = userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUserRole(Long id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
        Role role = roleRepository.findByRole(Role.RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role " + roleName
                        + " not found in the system."));
        user.getRoles().clear();
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new UserNotFoundException("User with email " + email
                        + " not found or has been deleted."));
    }

    @Override
    @Transactional
    public User updateUserProfile(String email, UpdateUserProfileRequestDto requestDto) {
        User user = getUserByEmail(email);
        user.setLogin(requestDto.login());
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        return userRepository.save(user);
    }
}
