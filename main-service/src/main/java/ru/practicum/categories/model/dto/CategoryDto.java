package ru.practicum.categories.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(min = 1, max = 50, message = "Имя категории не должно быть в пределах от 1 до 50 символов")
    private String name;
}
