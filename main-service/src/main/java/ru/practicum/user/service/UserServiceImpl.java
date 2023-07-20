package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User saveUser(UserDto dto) {
        User user = UserMapper.getUserFromDto(dto);
        return userRepository.save(user);
    }

    public List<User> getUsers(List<Long> userIds, int from, int size) {
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        return userRepository.getUsers(userIds, PageRequest.of(page, size));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
