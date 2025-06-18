package com.coffeeshop.coffeeshop_order_backend.model;

import com.coffeeshop.coffeeshop_order_backend.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "`order`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItem> items = new ArrayList<>();

    // Métodos de conveniencia
    public void addItem(Product product, int quantity) {
        OrderItem item = OrderItem.builder()
            .product(product)
            .quantity(quantity)
            .order(this)
            .build();
        items.add(item);
    }

    public double getTotal() {
        return items.stream()
            .mapToDouble(OrderItem::getSubtotal)
            .sum();
    }


    public void startPreparation() {
        this.status = OrderStatus.IN_PROGRESS;
    }

    public void markAsReady() {
        this.status = OrderStatus.READY;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
