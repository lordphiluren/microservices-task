package ru.sushchenko.supplier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.supplier.dto.ReviewRequest;
import ru.sushchenko.supplier.entity.Product;
import ru.sushchenko.supplier.entity.Review;
import ru.sushchenko.supplier.repo.ReviewRepo;
import ru.sushchenko.supplier.util.exception.NotFoundException;
import ru.sushchenko.supplier.util.mapper.ReviewMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final ProductService productService;
    private final ReviewMapper reviewMapper;
    @Transactional
    public Review add(ReviewRequest reviewDto) {
        Product product = productService.getById(reviewDto.getProductId());
        Review review = reviewMapper.toEntity(reviewDto);
        review.setProduct(product);
        return reviewRepo.save(review);
    }
    @Transactional
    public List<Review> getAll() {
        return reviewRepo.findAll();
    }
    @Transactional
    public Review getById(Long id) {
        return reviewRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Review with id:" + id + " doesn't exist"));
    }
    @Transactional
    public void deleteById(Long id) {
        reviewRepo.deleteById(id);
    }
    @Transactional
    public Review update(Review review) {
        return reviewRepo.save(review);
    }
}
