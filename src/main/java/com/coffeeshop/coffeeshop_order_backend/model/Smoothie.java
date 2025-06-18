package com.coffeeshop.coffeeshop_order_backend.model;

import com.coffeeshop.coffeeshop_order_backend.model.enums.Fruit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("SMOOTHIE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Smoothie extends Product {

    @ElementCollection(targetClass = Fruit.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "smoothie_fruits", joinColumns = @JoinColumn(name = "smoothie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "fruit")
    @Size(min = 1, message = "You must choose at least one fruit")
    private List<Fruit> fruits = new ArrayList<>();

    @Builder.Default
    private boolean hasYogurt = false;

    @Builder.Default
    private boolean hasMilk = false;
}