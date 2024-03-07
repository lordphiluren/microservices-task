package ru.sushchenko.supplier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sushchenko.supplier.dto.ProductRequest;
import ru.sushchenko.supplier.entity.Category;
import ru.sushchenko.supplier.entity.Product;
import ru.sushchenko.supplier.repo.ProductRepo;
import ru.sushchenko.supplier.repo.ProductSpecification;
import ru.sushchenko.supplier.util.exception.NotFoundException;
import ru.sushchenko.supplier.util.mapper.ProductMapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    @Transactional
    public Product add(ProductRequest productDto) {
        Category category = categoryService.getById(productDto.getCategoryId());
        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        return productRepo.save(product);
    }
    @Transactional
    public List<Product> getAll(Long categoryId, String search, BigDecimal minPrice,
                                BigDecimal maxPrice, Integer offset, Integer limit) {
        int offsetValue = offset != null ? offset : 0;
        int limitValue = limit != null ? limit : Integer.MAX_VALUE;
        Pageable pageable = PageRequest.of(offsetValue, limitValue, Sort.by("id"));
        return productRepo.findAll(ProductSpecification.filterProducts(categoryId, minPrice, maxPrice, search), pageable)
                .getContent();
    }
    @Transactional
    public Product getById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + " doesn't exist"));
    }
    @Transactional
    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }
    @Transactional
    public Product update(Long id, ProductRequest productDto) {
        Product product = getById(id);
        productMapper.mergeDtoIntoEntity(productDto, product);
        Category category = categoryService.getById(productDto.getCategoryId());
        product.setCategory(category);
        return productRepo.save(product);
    }
}
