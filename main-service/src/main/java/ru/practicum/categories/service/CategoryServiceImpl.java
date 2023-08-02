package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.exception.CategoryNotFoundException;
import ru.practicum.categories.model.CategoryMapper;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CategoryDto;
import ru.practicum.categories.repository.CategoriesRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    @Transactional
    public Categories saveCat(CategoryDto dto) {
        Categories categories = CategoryMapper.getCategoryFromDto(dto);
        return categoriesRepository.save(categories);
    }

    @Override
    public Categories getCat(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
    }


    @Override
    @Transactional
    public void deleteCat(Long catId) {
        categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
        categoriesRepository.deleteById(catId);
    }

    @Override
    public List<Categories> getAllCat(int from, int  size) {
        int page = from / size;
        int pageLast = from % size;
        if (pageLast > 0) {
            page++;
        }
        return categoriesRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    @Transactional
    public Categories patchCat(Long catId, CategoryDto dto) {
        Categories categories = categoriesRepository.findById(catId).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Категория с ID = %d не найдна", catId)));
        categories.setName(dto.getName());
        return  categoriesRepository.save(categories);
    }
}
