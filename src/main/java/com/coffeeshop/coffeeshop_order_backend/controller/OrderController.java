package com.coffeeshop.coffeeshop_order_backend.controller;

import com.coffeeshop.coffeeshop_order_backend.dto.OrderDto;
import com.coffeeshop.coffeeshop_order_backend.mapper.OrderMapper;
import com.coffeeshop.coffeeshop_order_backend.model.Order;
import com.coffeeshop.coffeeshop_order_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orderMapper.toDtoList(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.save(orderMapper.toEntity(orderDto));
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<OrderDto> startOrderPreparation(@PathVariable Long id) {
        Order order = orderService.startPreparation(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PutMapping("/{id}/ready")
    public ResponseEntity<OrderDto> markOrderAsReady(@PathVariable Long id) {
        Order order = orderService.markAsReady(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<OrderDto> completeOrder(@PathVariable Long id) {
        Order order = orderService.complete(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id) {
        Order order = orderService.cancel(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
