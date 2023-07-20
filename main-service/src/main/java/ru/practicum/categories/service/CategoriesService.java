package ru.practicum.categories.service;

import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CatDto;

public interface CategoriesService {
    Categories getCat(Long catId);

    Categories saveCat(CatDto dto);

    void deleteCat(Long catId);
}
