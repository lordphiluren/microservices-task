package ru.sushchenko.consumer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sushchenko.consumer.utils.validation.UpdateValidation;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {
    @NotNull(message = "Product name can't be empty")
    private String name;
    @Size(max = 512, message = "Product description can't be greater than 512", groups = {UpdateValidation.class})
    private String description;
    @NotNull(message = "Product price can't be empty")
    @Min(value = 0, message = "Price can't be negative", groups = {UpdateValidation.class})
    private BigDecimal price;
    @NotNull(message = "Product category can't be empty")
    private Long categoryId;
}
