package ru.sushchenko.supplier.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sushchenko.supplier.entity.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
}
