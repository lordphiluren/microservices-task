package ru.sushchenko.consumer.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.consumer.dto.ProductRequest;
import ru.sushchenko.consumer.dto.ProductResponse;
import ru.sushchenko.consumer.service.ProductService;
import ru.sushchenko.consumer.utils.validation.UpdateValidation;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Controller for manipulating products")
public class ProductController {
    private final ProductService productService;
    @Operation(
            summary = "Get list of products",
            description = "Get list of products with full filter and pagination"
    )
    @GetMapping
    public List<ProductResponse> getAllProducts(@RequestParam(name="category", required = false) Long categoryId,
                                                @RequestParam(name="search", required = false) String search,
                                                @RequestParam(name="min_price", required = false) BigDecimal minPrice,
                                                @RequestParam(name="max_price", required = false) BigDecimal maxPrice,
                                                @RequestParam(name="offset", required = false) Integer offset,
                                                @RequestParam(name="limit", required = false) Integer limit) {
        return productService.getAll(categoryId, search, minPrice, maxPrice, offset, limit);
    }
    @Operation(
            summary = "Get product by id"
    )
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }
    @Operation(
            summary = "Adds product"
    )
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productDto) {
        return ResponseEntity.ok(productService.add(productDto));
    }
    @Operation(
            summary = "Delete product by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Product with id: " + id + " deleted");
    }
    @Operation(
            summary = "Update product by id"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                            @Validated({UpdateValidation.class}) @RequestBody ProductRequest productDto) {
        return ResponseEntity.ok(productService.update(id, productDto));
    }
}
