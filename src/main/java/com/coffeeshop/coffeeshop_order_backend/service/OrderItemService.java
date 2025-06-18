package com.coffeeshop.coffeeshop_order_backend.service;

import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;
import com.coffeeshop.coffeeshop_order_backend.model.OrderItem;
import com.coffeeshop.coffeeshop_order_backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + id));
    }

    @Transactional
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public void deleteById(Long id) {
        OrderItem orderItem = findById(id);
        orderItemRepository.delete(orderItem);
    }

    @Transactional
    public OrderItem update(Long id, OrderItem orderItemDetails) {
        OrderItem orderItem = findById(id);
        orderItem.setProduct(orderItemDetails.getProduct());
        orderItem.setQuantity(orderItemDetails.getQuantity());
        orderItem.setOrder(orderItemDetails.getOrder());
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public List<OrderItem> saveAll(List<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }
}
