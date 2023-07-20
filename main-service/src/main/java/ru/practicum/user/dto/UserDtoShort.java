package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoShort {
    private Long id;
    private String name;
}
