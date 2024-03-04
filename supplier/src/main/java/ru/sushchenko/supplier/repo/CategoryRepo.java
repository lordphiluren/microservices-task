package ru.sushchenko.supplier.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.entity.Product;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
}
