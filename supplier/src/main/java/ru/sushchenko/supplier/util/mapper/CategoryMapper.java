package ru.sushchenko.supplier.util.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.sushchenko.supplier.dto.CategoryRequest;
import ru.sushchenko.supplier.dto.CategoryResponse;
import ru.sushchenko.supplier.entity.Category;
@Component
@RequiredArgsConstructor
public class CategoryMapper implements Mapper<Category, CategoryResponse, CategoryRequest> {
    private final ModelMapper mapper;

    @Override
    public CategoryResponse toDto(Category category) {
        return mapper.map(category, CategoryResponse.class);
    }

    @Override
    public Category toEntity(CategoryRequest categoryRequest) {
        return mapper.map(categoryRequest, Category.class);
    }
    public void mergeDtoIntoEntity(CategoryRequest dto, Category category) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(dto, category);
    }
}
