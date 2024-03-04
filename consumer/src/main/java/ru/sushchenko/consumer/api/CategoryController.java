package ru.sushchenko.consumer.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.consumer.dto.CategoryRequest;
import ru.sushchenko.consumer.dto.CategoryResponse;
import ru.sushchenko.consumer.service.CategoryService;
import ru.sushchenko.consumer.utils.validation.UpdateValidation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Controller for manipulating categories")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    @Operation(
            summary = "Get list of categories"
    )
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAll();
    }
    @Operation(
            summary = "Get category by id"
    )
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }
    @Operation(
            summary = "Adds category"
    )
    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryDto) {
        return ResponseEntity.ok(categoryService.add(categoryDto));
    }
    @Operation(
            summary = "Delete category by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok("Category with id: " + id + " deleted");
    }
    @Operation(
            summary = "Update category by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @Validated({UpdateValidation.class}) @RequestBody CategoryRequest categoryDto) {
        return ResponseEntity.ok(categoryService.update(id, categoryDto));
    }
}
