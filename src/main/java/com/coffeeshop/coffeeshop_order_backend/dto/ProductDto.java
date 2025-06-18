package com.coffeeshop.coffeeshop_order_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    
    private String description;

    private String productType;
    private String coffeeType;
    private String teaType;
    private String[] smoothieIngredients;
}
