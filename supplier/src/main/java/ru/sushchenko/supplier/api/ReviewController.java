package ru.sushchenko.supplier.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.supplier.dto.ReviewRequest;
import ru.sushchenko.supplier.entity.Product;
import ru.sushchenko.supplier.entity.Review;
import ru.sushchenko.supplier.service.ProductService;
import ru.sushchenko.supplier.service.ReviewService;
import ru.sushchenko.supplier.util.mapper.ReviewMapper;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Controller for manipulating products' reviews")
public class ReviewController {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;
    @Operation(
            summary = "Adds review to product"
    )
    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewRequest reviewDto) {
        return ResponseEntity.ok(reviewMapper.toDto(reviewService.add(reviewDto)));
    }
    @Operation(
            summary = "Delete review by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.ok("Review with id: " + id + " deleted");
    }
}
