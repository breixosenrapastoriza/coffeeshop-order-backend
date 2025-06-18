package com.coffeeshop.coffeeshop_order_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
    
    // Solo para respuestas, no para solicitudes
    private String productName;
    private Double productPrice;
    private Double subtotal;
}
