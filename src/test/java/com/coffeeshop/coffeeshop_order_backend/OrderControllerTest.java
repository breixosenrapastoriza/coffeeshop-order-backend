package com.coffeeshop.coffeeshop_order_backend;

import com.coffeeshop.coffeeshop_order_backend.controller.OrderController;
import com.coffeeshop.coffeeshop_order_backend.dto.OrderDto;
import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;
import com.coffeeshop.coffeeshop_order_backend.mapper.OrderMapper;
import com.coffeeshop.coffeeshop_order_backend.model.Order;
import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import com.coffeeshop.coffeeshop_order_backend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    private Order order;
    private OrderDto orderDto;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.IN_PROGRESS)
                .orderDate(LocalDateTime.now())
                .build();

        orderDto = OrderDto.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .total(5.99)
                .build();
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        // Arrange
        List<Order> orders = Arrays.asList(order);
        when(orderService.findAll()).thenReturn(orders);
        when(orderMapper.toDtoList(orders)).thenReturn(Arrays.asList(orderDto));

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerName", is(orderDto.getCustomerName())));

        verify(orderService, times(1)).findAll();
    }

    @Test
    void getOrderById_ExistingId_ShouldReturnOrder() throws Exception {
        // Arrange
        when(orderService.findById(1L)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerName", is(orderDto.getCustomerName())));

        verify(orderService, times(1)).findById(1L);
    }

    @Test
    void createOrder_ValidOrder_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderService.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is(orderDto.getCustomerName())));

        verify(orderService, times(1)).save(any(Order.class));
    }

    @Test
    void completeOrder_ValidId_ShouldReturnCompletedOrder() throws Exception {
        // Arrange
        Order completedOrder = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.COMPLETED)
                .orderDate(order.getOrderDate())
                .build();

        OrderDto completedOrderDto = OrderDto.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.COMPLETED)
                .createdAt(orderDto.getCreatedAt())
                .total(5.99)
                .build();

        when(orderService.complete(1L)).thenReturn(completedOrder);
        when(orderMapper.toDto(completedOrder)).thenReturn(completedOrderDto);

        // Act & Assert
        mockMvc.perform(put("/api/orders/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(OrderStatus.COMPLETED.toString())));

        verify(orderService, times(1)).complete(1L);
    }

    @Test
    void markOrderAsReady_ValidId_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        Order readyOrder = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.READY)
                .orderDate(order.getOrderDate())
                .build();

        OrderDto readyOrderDto = OrderDto.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.READY)
                .createdAt(orderDto.getCreatedAt())
                .total(5.99)
                .build();

        when(orderService.markAsReady(1L)).thenReturn(readyOrder);
        when(orderMapper.toDto(readyOrder)).thenReturn(readyOrderDto);

        // Act & Assert
        mockMvc.perform(put("/api/orders/1/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(OrderStatus.READY.toString())));

        verify(orderService, times(1)).markAsReady(1L);
    }

    @Test
    void cancelOrder_ValidId_ShouldReturnCanceledOrder() throws Exception {
        // Arrange
        Order canceledOrder = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.CANCELLED)
                .orderDate(order.getOrderDate())
                .build();

        OrderDto canceledOrderDto = OrderDto.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.CANCELLED)
                .createdAt(orderDto.getCreatedAt())
                .total(5.99)
                .build();

        when(orderService.cancel(1L)).thenReturn(canceledOrder);
        when(orderMapper.toDto(canceledOrder)).thenReturn(canceledOrderDto);

        // Act & Assert
        mockMvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(OrderStatus.CANCELLED.toString())));

        verify(orderService, times(1)).cancel(1L);
    }

    @Test
    void deleteOrder_ExistingId_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteById(1L);
    }

    @Test
    void getOrderById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(orderService.findById(999L)).thenThrow(new ResourceNotFoundException("Order not found"));

        // Act & Assert
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).findById(999L);
    }
}