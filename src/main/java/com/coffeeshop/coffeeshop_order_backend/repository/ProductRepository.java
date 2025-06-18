package com.coffeeshop.coffeeshop_order_backend.repository;

import com.coffeeshop.coffeeshop_order_backend.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findByNameContainingIgnoreCase(String name);
    
}
