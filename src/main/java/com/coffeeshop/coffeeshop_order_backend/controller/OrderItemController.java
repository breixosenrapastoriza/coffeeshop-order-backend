package com.coffeeshop.coffeeshop_order_backend.controller;

import com.coffeeshop.coffeeshop_order_backend.dto.OrderItemDto;
import com.coffeeshop.coffeeshop_order_backend.mapper.OrderItemMapper;
import com.coffeeshop.coffeeshop_order_backend.model.OrderItem;
import com.coffeeshop.coffeeshop_order_backend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getAllOrderItems() {
        List<OrderItem> items = orderItemService.findAll();
        return ResponseEntity.ok(orderItemMapper.toDtoList(items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        OrderItem item = orderItemService.findById(id);
        return ResponseEntity.ok(orderItemMapper.toDto(item));
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> createOrderItem(@RequestBody OrderItemDto itemDto) {
        OrderItem item = orderItemService.save(orderItemMapper.toEntity(itemDto));
        return ResponseEntity.ok(orderItemMapper.toDto(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateOrderItem(
            @PathVariable Long id, 
            @RequestBody OrderItemDto itemDto) {
        OrderItem item = orderItemMapper.toEntity(itemDto);
        OrderItem updatedItem = orderItemService.update(id, item);
        return ResponseEntity.ok(orderItemMapper.toDto(updatedItem));
    }

    @DeleteMapping("/{id}")
    public void deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteById(id);
    }
}
