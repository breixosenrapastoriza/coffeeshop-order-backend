package com.coffeeshop.coffeeshop_order_backend.mapper;

import com.coffeeshop.coffeeshop_order_backend.dto.OrderItemDto;
import com.coffeeshop.coffeeshop_order_backend.model.OrderItem;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ProductService productService;

    public OrderItemDto toDto(OrderItem item) {
        if (item == null) {
            return null;
        }
        
        return OrderItemDto.builder()
            .id(item.getId())
            .productId(item.getProduct().getId())
            .quantity(item.getQuantity())
            .productName(item.getProduct().getName())
            .productPrice(item.getProduct().getPrice())
            .subtotal(item.getSubtotal())
            .build();
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> items) {
        return items.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public OrderItem toEntity(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = productService.findById(dto.getProductId());
        return OrderItem.builder()
            .id(dto.getId())
            .product(product)
            .quantity(dto.getQuantity())
            .build();
    }
}
