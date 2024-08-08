package mate.academy.taskmanagement.service;

import mate.academy.taskmanagement.dto.user.UpdateUserProfileRequestDto;
import mate.academy.taskmanagement.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagement.model.User;

public interface UserService {
    User register(UserRegistrationRequestDto request);

    User updateUserRole(Long id, String roleName);

    User getUserByEmail(String email);

    User updateUserProfile(String email, UpdateUserProfileRequestDto requestDto);
}
