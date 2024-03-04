package ru.sushchenko.supplier.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.sushchenko.supplier.entity.Product;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "product-entity-graph-category_reviews")
    Optional<Product> findById(Long id);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "product-entity-graph-category_reviews")
    Page<Product> findAll(Pageable pageable);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "product-entity-graph-category_reviews")
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}
