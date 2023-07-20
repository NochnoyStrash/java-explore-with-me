package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto user);

    List<User> getUsers(List<Long> userIds, int from, int size);

    void deleteUser(Long userId);
}
