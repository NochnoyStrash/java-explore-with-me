package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 250, message = "длинна имейла должна быть от 2 до 250 символов")
    private String name;
    @NotNull
    @Email
    @Size(min = 6, max = 254, message = "длинна имейла должна быть от 6 до 254 символов")
    private String email;
}
