package com.coffeeshop.coffeeshop_order_backend;

import com.coffeeshop.coffeeshop_order_backend.controller.ProductController;
import com.coffeeshop.coffeeshop_order_backend.dto.ProductDto;
import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;
import com.coffeeshop.coffeeshop_order_backend.mapper.ProductMapper;
import com.coffeeshop.coffeeshop_order_backend.model.Coffee;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private Product product;
    private ProductDto productDto;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        product = Coffee.builder()
                .id(1L)
                .name("Espresso")
                .price(2.5)
                .description("Strong coffee")
                .build();

        productDto = ProductDto.builder().id(1L).name("Espresso").price(2.5).description("Strong coffee").build();
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product);
        when(productService.findAll()).thenReturn(products);
        when(productMapper.toDtoList(products)).thenReturn(Arrays.asList(productDto));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(productDto.getName())));

        verify(productService, times(1)).findAll();
    }

    @Test
    void getProductById_ExistingId_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productService.findById(1L)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(productDto.getName())));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void createProduct_ValidProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())));

        verify(productService, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_ValidIdAndProduct_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productService.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(productDto.getName())));

        verify(productService, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ExistingId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(productService.findById(1L)).thenReturn(product);
        doNothing().when(productService).delete(any(Product.class));

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).findById(1L);
        verify(productService, times(1)).delete(any(Product.class));
    }

    @Test
    void getProductById_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productService.findById(999L)).thenThrow(new ResourceNotFoundException("Product not found"));

        // Act & Assert
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(999L);
    }
}