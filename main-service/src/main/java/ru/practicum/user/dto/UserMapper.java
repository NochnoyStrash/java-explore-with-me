package ru.practicum.user.dto;

import ru.practicum.user.model.User;

public class UserMapper {

    private UserMapper() {

    }

    public static User getUserFromDto(UserDto dto) {
        String[] partsName = dto.getName().split(" ");
        return User.builder()
                .email(dto.getEmail())
                .firstName(partsName[0])
                .lastName(partsName[1])
                .build();
    }

    public static UserDto getUserDtoFromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public static UserDtoShort getUserShorts(User user) {
        return UserDtoShort.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .build();
    }


}
