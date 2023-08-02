package ru.practicum.categories.service;

import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CategoryDto;

import java.util.List;

public interface CategoriesService {
    Categories getCat(Long catId);

    List<Categories> getAllCat(int from, int  size);

    Categories saveCat(CategoryDto dto);

    void deleteCat(Long catId);

    Categories patchCat(Long catId, CategoryDto dto);
}
