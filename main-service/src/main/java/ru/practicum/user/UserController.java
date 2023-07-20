package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size) {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody UserDto dto) {
        return userService.saveUser(dto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
