package com.coffeeshop.coffeeshop_order_backend.dto;

import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    
    @NotNull(message = "Customer name is required")
    private String customerName;
    
    private String customerEmail;
    private String customerPhone;
    
    @NotNull(message = "Order status is required")
    private OrderStatus status;
    
    @Valid
    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemDto> items;
    
    // Solo para respuestas
    private Double total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
