package ru.practicum.categories.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatDto {
    private Long id;
    private String name;
}
