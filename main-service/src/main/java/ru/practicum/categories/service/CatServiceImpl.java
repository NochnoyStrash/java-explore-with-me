package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.CatMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CatDto;
import ru.practicum.categories.repository.CategoriesRepository;

@Service
@AllArgsConstructor
public class CatServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public Categories saveCat(CatDto dto) {
        Categories categories = CatMapper.getCatFromDto(dto);
        return categoriesRepository.save(categories);
    }

    public Categories getCat(Long catId) {
        return categoriesRepository.findById(catId).get();
    }

    public void deleteCat(Long catId) {
        categoriesRepository.deleteById(catId);
    }
}
