package com.coffeeshop.coffeeshop_order_backend.service;

import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;
import com.coffeeshop.coffeeshop_order_backend.model.Order;
import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import com.coffeeshop.coffeeshop_order_backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteById(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional
    public Order startPreparation(Long id) {
        Order order = findById(id);
        order.startPreparation();
        return orderRepository.save(order);
    }

    @Transactional
    public Order markAsReady(Long id) {
        Order order = findById(id);
        order.markAsReady();
        return orderRepository.save(order);
    }

    @Transactional
    public Order complete(Long id) {
        Order order = findById(id);
        order.complete();
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long id) {
        Order order = findById(id);
        order.cancel();
        return orderRepository.save(order);
    }
}
