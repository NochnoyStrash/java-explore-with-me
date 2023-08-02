package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
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
        if (userIds != null) {
            return userRepository.getUsers(userIds, PageRequest.of(page, size));
        }
        return userRepository.findAll(PageRequest.of(page,size)).toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с ID =  %d не найден", userId)));
        userRepository.deleteById(userId);
    }
}
