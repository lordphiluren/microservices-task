package ru.sushchenko.supplier.service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.supplier.entity.Review;
import ru.sushchenko.supplier.repo.ReviewRepo;
import ru.sushchenko.supplier.util.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;
    @Transactional
    public Review add(Review review) {
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
