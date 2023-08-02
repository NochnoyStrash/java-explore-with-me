package ru.practicum.categories.model;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {

    public static Categories getCategoryFromDto(CategoryDto dto) {
        return Categories.builder()
                .name(dto.getName())
                .build();
    }

    public static CategoryDto getDtoFromCategory(Categories categories) {
        return CategoryDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }
}
