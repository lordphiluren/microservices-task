package ru.sushchenko.supplier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.supplier.dto.ProductRequest;
import ru.sushchenko.supplier.dto.ProductResponse;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.entity.Product;
import ru.sushchenko.supplier.service.CategoryService;
import ru.sushchenko.supplier.service.ProductService;
import ru.sushchenko.supplier.util.mapper.CategoryMapper;
import ru.sushchenko.supplier.util.mapper.ProductMapper;
import ru.sushchenko.supplier.util.validation.UpdateValidation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Controller for manipulating products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Operation(
            summary = "Adds product"
    )
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productDto) {
        Category category = categoryService.getById(productDto.getCategoryId());
        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        Product savedProduct = productService.add(product);
        return ResponseEntity.ok(productMapper.toDto(savedProduct));
    }
    @Operation(
            summary = "Get list of products",
            description = "Get list of products with full filter and pagination"
    )
    @GetMapping
    public List<ProductResponse> getAll(@RequestParam(name="category", required = false) Long categoryId,
                                        @RequestParam(name="search", required = false) String search,
                                        @RequestParam(name="min_price", required = false) BigDecimal minPrice,
                                        @RequestParam(name="max_price", required = false) BigDecimal maxPrice,
                                        @RequestParam(name="offset", required = false) Integer offset,
                                        @RequestParam(name="limit", required = false) Integer limit) {
        return productService.getAll(categoryId, search, minPrice, maxPrice, offset, limit).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
    @Operation(
            summary = "Get product by id"
    )
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productMapper.toDto(productService.getById(id));
    }
    @Operation(
            summary = "Delete product by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Place with id: " + id + "deleted");
    }
    @Operation(
            summary = "Update product by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id,
                                        @Validated({UpdateValidation.class}) @RequestBody ProductRequest productDto) {
        Product product = productService.getById(id);
        productMapper.mergeDtoIntoEntity(productDto, product);
        Category category = categoryService.getById(productDto.getCategoryId());
        product.setCategory(category);
        return ResponseEntity.ok(productMapper.toDto(productService.update(product)));
    }

}
