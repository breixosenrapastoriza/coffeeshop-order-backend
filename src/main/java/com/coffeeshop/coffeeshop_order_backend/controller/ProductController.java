package com.coffeeshop.coffeeshop_order_backend.controller;

import com.coffeeshop.coffeeshop_order_backend.dto.ProductDto;
import com.coffeeshop.coffeeshop_order_backend.mapper.ProductMapper;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(productMapper.toDtoList(products));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.save(productMapper.toEntity(productDto));
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product.setId(id);
        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        productService.delete(product);
    }
}
