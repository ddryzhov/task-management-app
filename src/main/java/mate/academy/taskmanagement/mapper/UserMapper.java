package mate.academy.taskmanagement.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.taskmanagement.config.MapperConfig;
import mate.academy.taskmanagement.dto.user.UserProfileResponseDto;
import mate.academy.taskmanagement.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagement.dto.user.UserResponseDto;
import mate.academy.taskmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "roles", expression = "java(mapAuthorities(user.getAuthorities()))")
    UserProfileResponseDto toProfileDto(User user);

    default Set<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
