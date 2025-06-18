package com.coffeeshop.coffeeshop_order_backend.mapper;

import com.coffeeshop.coffeeshop_order_backend.dto.OrderDto;
import com.coffeeshop.coffeeshop_order_backend.dto.OrderItemDto;
import com.coffeeshop.coffeeshop_order_backend.model.Order;
import com.coffeeshop.coffeeshop_order_backend.model.OrderItem;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductService productService;

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(item -> OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .productName(item.getProduct().getName())
                .productPrice(item.getProduct().getPrice())
                .subtotal(item.getSubtotal())
                .build())
            .collect(Collectors.toList());

        return OrderDto.builder()
            .id(order.getId())
            .customerName(order.getCustomerName())
            .status(order.getStatus())
            .items(itemDtos)
            .total(order.getTotal())
            .createdAt(order.getOrderDate())
            .build();
    }

    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Order order = Order.builder()
            .id(dto.getId())
            .customerName(dto.getCustomerName())
            .status(dto.getStatus())
            .build();

        if (dto.getItems() != null) {
            for (OrderItemDto itemDto : dto.getItems()) {
                Product product = productService.findById(itemDto.getProductId());
                OrderItem item = OrderItem.builder()
                    .id(itemDto.getId())
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .build();
                order.addItem(product, item.getQuantity());
            }
        }

        return order;
    }
}
