package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.exception.CategoryNotFoundException;
import ru.practicum.categories.model.CatMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CatDto;
import ru.practicum.categories.repository.CategoriesRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CatServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public Categories saveCat(CatDto dto) {
        Categories categories = CatMapper.getCatFromDto(dto);
        return categoriesRepository.save(categories);
    }

    public Categories getCat(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
    }

    public void deleteCat(Long catId) {
        categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
        categoriesRepository.deleteById(catId);
    }

    public List<Categories> getAllCat(int from, int  size) {
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        return categoriesRepository.findAll(PageRequest.of(page, size)).toList();
    }

    public Categories patchCat(Long catId, CatDto dto) {
        Categories categories = categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
        categories.setName(dto.getName());
        return  categoriesRepository.save(categories);
    }
}
