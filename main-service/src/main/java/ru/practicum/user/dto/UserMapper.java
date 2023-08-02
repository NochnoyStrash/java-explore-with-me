package ru.practicum.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;


@UtilityClass
public class UserMapper {

    public static User getUserFromDto(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public static UserDto getUserDtoFromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDtoShort getUserShorts(User user) {
        return UserDtoShort.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }


}
