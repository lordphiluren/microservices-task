package ru.sushchenko.supplier.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.sushchenko.supplier.entity.Product;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductSpecification {
    public static Specification<Product> filterProducts(Long categoryId, BigDecimal lowerBound,
                                                        BigDecimal upperBound, String search) {
        Specification<Product> spec = Specification.where(null);
        BigDecimal lowerBoundValue = lowerBound != null ? lowerBound : BigDecimal.valueOf(0);
        BigDecimal upperBoundValue = upperBound != null ? upperBound : BigDecimal.valueOf(Integer.MAX_VALUE);
        if(categoryId != null) {
            spec = spec.and(belongsToCategory(categoryId));
        }
        if(search != null && !search.isEmpty()) {
            spec = spec.and(nameOrDescriptionLike(search));
        }
        spec = spec.and(priceBetween(lowerBoundValue, upperBoundValue));
        return spec;
    }
    private static Specification<Product> belongsToCategory(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), id);
    }
    private static Specification<Product> priceBetween(BigDecimal lowerBound, BigDecimal upperBound) {
        return (root, query, criteriaBuilder)-> criteriaBuilder.between(root.get("price"), lowerBound, upperBound);
    }
    private static Specification<Product> nameOrDescriptionLike(String search) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("name"), "%" + search + "%"),
                criteriaBuilder.like(root.get("description"), "%" + search + "%"));
    }
}
