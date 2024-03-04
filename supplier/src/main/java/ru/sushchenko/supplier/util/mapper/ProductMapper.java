package ru.sushchenko.supplier.util.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.sushchenko.supplier.dto.ProductRequest;
import ru.sushchenko.supplier.dto.ProductResponse;
import ru.sushchenko.supplier.dto.ReviewResponse;
import ru.sushchenko.supplier.entity.Product;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper implements Mapper<Product, ProductResponse, ProductRequest> {
    private final ModelMapper mapper;
    private final CategoryMapper categoryMapper;
    private final ReviewMapper reviewMapper;
    public Product toEntity(ProductRequest dto) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(dto, Product.class);
    }
    public ProductResponse toDto(Product product) {
        ProductResponse dto =  mapper.map(product, ProductResponse.class);
        Set<ReviewResponse> reviewsDto = product.getReviews().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toSet());
        dto.setCategory(categoryMapper.toDto(product.getCategory()));
        dto.setReviews(reviewsDto);
        return dto;
    }
    public void mergeDtoIntoEntity(ProductRequest dto, Product product) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(dto, product);
    }
}
