package ru.practicum.categories;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CatDto;
import ru.practicum.categories.service.CategoriesService;

import javax.validation.constraints.Positive;

@RestController
@AllArgsConstructor
@RequestMapping("")
public class CatController {
    private final CategoriesService categoriesService;

    @PostMapping("/admin/categories")
    public Categories saveCat(@RequestBody CatDto dto) {
        return categoriesService.saveCat(dto);
    }

    @GetMapping("/admin/categories/{catId}")
    public Categories getCat(@PathVariable @Positive long catId) {
        return categoriesService.getCat(catId);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public  void deleteCat(@PathVariable @Positive long catId) {
        categoriesService.deleteCat(catId);
    }
}
