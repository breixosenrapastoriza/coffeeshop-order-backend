package com.coffeeshop.coffeeshop_order_backend.mapper;

import com.coffeeshop.coffeeshop_order_backend.dto.ProductDto;
import com.coffeeshop.coffeeshop_order_backend.model.Coffee;
import com.coffeeshop.coffeeshop_order_backend.model.Product;
import com.coffeeshop.coffeeshop_order_backend.model.Smoothie;
import com.coffeeshop.coffeeshop_order_backend.model.Tea;
import com.coffeeshop.coffeeshop_order_backend.model.enums.CoffeeType;
import com.coffeeshop.coffeeshop_order_backend.model.enums.Fruit;
import com.coffeeshop.coffeeshop_order_backend.model.enums.TeaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .productType(product.getClass().getSimpleName().toUpperCase())
            .coffeeType(product instanceof Coffee ? String.valueOf(((Coffee) product).getCoffeeType()) : null)
            .teaType(product instanceof Tea ? String.valueOf(((Tea) product).getTeaType()) : null)
            .fruits(product instanceof Smoothie ? ((Smoothie) product).getFruits()
                .stream()
                .map(Fruit::name)
                .toArray(String[]::new) : null)
            .build();
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        return switch (dto.getProductType()) {
            case "COFFEE" -> Coffee.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .description(dto.getDescription())
                    .coffeeType(CoffeeType.valueOf(dto.getCoffeeType()))
                    .build();
            case "TEA" -> Tea.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .description(dto.getDescription())
                    .teaType(TeaType.valueOf(dto.getTeaType()))
                    .build();
            case "SMOOTHIE" -> Smoothie.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .description(dto.getDescription())
                    .fruits(Arrays.stream(dto.getFruits())
                            .map(Fruit::valueOf)
                            .collect(Collectors.toList()))
                    .build();
            default -> throw new IllegalArgumentException("Unknown product type: " + dto.getProductType());
        };
    }
}
