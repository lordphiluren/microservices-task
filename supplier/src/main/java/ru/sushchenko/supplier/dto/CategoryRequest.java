package ru.sushchenko.supplier.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRequest {
    @NotNull(message = "Category name can't be null")
    @Size(max = 128, message = "Category name length can't be greater than 128")
    private String name;
}
