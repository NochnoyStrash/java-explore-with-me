package ru.practicum.categories.model;

import ru.practicum.categories.model.dto.CatDto;

public class CatMapper {
    private CatMapper() {

    }

    public static Categories getCatFromDto(CatDto dto) {
        return Categories.builder()
                .name(dto.getName())
                .build();
    }

    public static CatDto getDtoFromCat(Categories categories) {
        return CatDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }
}
