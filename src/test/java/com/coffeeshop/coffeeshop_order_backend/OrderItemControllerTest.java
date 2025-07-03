package com.coffeeshop.coffeeshop_order_backend;

import com.coffeeshop.coffeeshop_order_backend.controller.OrderItemController;
import com.coffeeshop.coffeeshop_order_backend.dto.OrderItemDto;
import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;
import com.coffeeshop.coffeeshop_order_backend.mapper.OrderItemMapper;
import com.coffeeshop.coffeeshop_order_backend.model.Coffee;
import com.coffeeshop.coffeeshop_order_backend.model.Order;
import com.coffeeshop.coffeeshop_order_backend.model.OrderItem;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import com.coffeeshop.coffeeshop_order_backend.service.OrderItemService;
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

import java.math.BigDecimal;
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
class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private OrderItemMapper orderItemMapper;

    private Order order;
    private Product product;
    private OrderItem orderItem;
    private OrderItemDto orderItemDto;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setUp() {
        // Create test order
        order = Order.builder()
                .id(1L)
                .customerName("John Doe")
                .status(OrderStatus.IN_PROGRESS)
                .orderDate(LocalDateTime.now())
                .build();

        // Create test product
        product = Coffee.builder()
                .id(1L)
                .name("Espresso")
                .price(2.5)
                .description("Strong coffee")
                .build();

        // Create test order item
        orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(2)
                .build();

        // Create test DTO
        orderItemDto = OrderItemDto.builder()
                .id(1L)
                .orderId(1L)
                .productId(1L)
                .quantity(2)
                .build();
    }

    @Test
    void getAllOrderItems_ShouldReturnListOfOrderItems() throws Exception {
        // Arrange
        List<OrderItem> orderItems = Arrays.asList(orderItem);
        when(orderItemService.findAll()).thenReturn(orderItems);
        when(orderItemMapper.toDtoList(orderItems)).thenReturn(Arrays.asList(orderItemDto));

        // Act & Assert
        mockMvc.perform(get("/api/order-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].quantity", is(2)));

        verify(orderItemService, times(1)).findAll();
    }

    @Test
    void getOrderItemById_ExistingId_ShouldReturnOrderItem() throws Exception {
        // Arrange
        when(orderItemService.findById(1L)).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        // Act & Assert
        mockMvc.perform(get("/api/order-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(2)));

        verify(orderItemService, times(1)).findById(1L);
    }

    @Test
    void createOrderItem_ValidOrderItem_ShouldReturnCreatedOrderItem() throws Exception {
        // Arrange
        when(orderItemMapper.toEntity(any(OrderItemDto.class))).thenReturn(orderItem);
        when(orderItemService.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(orderItemDto);

        // Act & Assert
        mockMvc.perform(post("/api/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(2)));

        verify(orderItemService, times(1)).save(any(OrderItem.class));
    }

    @Test
    void updateOrderItem_ValidIdAndOrderItem_ShouldReturnUpdatedOrderItem() throws Exception {
        // Arrange
        OrderItem updatedOrderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(3)  // Updated quantity
                .build();

        OrderItemDto updatedOrderItemDto = OrderItemDto.builder()
                .id(1L)
                .orderId(1L)
                .productId(1L)
                .quantity(3)  // Updated quantity
                .build();

        when(orderItemService.update(eq(1L), any(OrderItem.class))).thenReturn(updatedOrderItem);
        when(orderItemMapper.toEntity(any(OrderItemDto.class))).thenReturn(updatedOrderItem);
        when(orderItemMapper.toDto(updatedOrderItem)).thenReturn(updatedOrderItemDto);

        // Act & Assert
        mockMvc.perform(put("/api/order-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.quantity", is(3)));  // Verify updated quantity

        verify(orderItemService, times(1)).update(eq(1L), any(OrderItem.class));
    }

    @Test
    void deleteOrderItem_ExistingId_ShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/order-items/1"))
                .andExpect(status().isOk());

        verify(orderItemService, times(1)).deleteById(1L);
    }

    @Test
    void getOrderItemById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(orderItemService.findById(999L)).thenThrow(new ResourceNotFoundException("OrderItem not found"));

        // Act & Assert
        mockMvc.perform(get("/api/order-items/999"))
                .andExpect(status().isNotFound());

        verify(orderItemService, times(1)).findById(999L);
    }
}