package ru.sushchenko.supplier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.supplier.dto.CategoryRequest;
import ru.sushchenko.supplier.dto.CategoryResponse;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.service.CategoryService;
import ru.sushchenko.supplier.util.mapper.CategoryMapper;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Controller for manipulating categories")
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    @Operation(
            summary = "Adds category"
    )
    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest categoryDto) {
        return ResponseEntity.ok(categoryMapper.toDto(categoryService.add(categoryMapper.toEntity(categoryDto))));
    }
    @Operation(
            summary = "Get list of categories"
    )
    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
    @Operation(
            summary = "Get category by id"
    )
    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryMapper.toDto(categoryService.getById(id));
    }
    @Operation(
            summary = "Delete category by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok("Category with id: " + id + " deleted");
    }
    @Operation(
            summary = "Update category by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryDto) {
        Category category = categoryService.getById(id);
        categoryMapper.mergeDtoIntoEntity(categoryDto, category);
        return ResponseEntity.ok(categoryMapper.toDto(categoryService.update(category)));
    }
}
