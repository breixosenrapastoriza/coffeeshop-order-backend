package com.coffeeshop.coffeeshop_order_backend.service;

import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coffeeshop.coffeeshop_order_backend.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> findAllByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @Transactional(readOnly = true)
    public List<Product> findAllByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }

    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }


    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

}
