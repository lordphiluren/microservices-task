package ru.sushchenko.consumer.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sushchenko.consumer.dto.ReviewRequest;
import ru.sushchenko.consumer.service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Controller for manipulating products' reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @Operation(
            summary = "Adds review to product"
    )
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ReviewRequest reviewDto) {
        return ResponseEntity.ok(reviewService.add(reviewDto));
    }
    @Operation(
            summary = "Delete review by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.ok("Review with id: " + id + " deleted");
    }
}
