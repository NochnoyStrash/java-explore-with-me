package ru.practicum.categories;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.model.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("")
@Validated
public class CategoryController {
    private final CategoriesService categoriesService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Categories saveCat(@RequestBody @Valid CategoryDto dto) {
        return categoriesService.saveCat(dto);
    }

    @GetMapping("/categories/{catId}")
    public Categories getCat(@PathVariable @Positive long catId) {
        return categoriesService.getCat(catId);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deleteCat(@PathVariable @Positive long catId) {
        categoriesService.deleteCat(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public Categories patchCat(@PathVariable long catId, @Valid @RequestBody CategoryDto dto) {
        return categoriesService.patchCat(catId, dto);
    }

    @GetMapping("/categories")
    public List<Categories> getAllCat(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return categoriesService.getAllCat(from, size);
    }
}
