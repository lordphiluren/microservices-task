package ru.sushchenko.supplier.util.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import ru.sushchenko.supplier.dto.ReviewRequest;
import ru.sushchenko.supplier.dto.ReviewResponse;
import ru.sushchenko.supplier.entity.Review;

@Component
@RequiredArgsConstructor
public class ReviewMapper implements Mapper<Review, ReviewResponse, ReviewRequest> {
    private final ModelMapper mapper;
    @Override
    public ReviewResponse toDto(Review review) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(review, ReviewResponse.class);
    }

    @Override
    public Review toEntity(ReviewRequest reviewRequest) {
        return mapper.map(reviewRequest, Review.class);
    }
    public void mergeDtoIntoEntity(ReviewRequest dto, Review review) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(dto, review);
    }
}
