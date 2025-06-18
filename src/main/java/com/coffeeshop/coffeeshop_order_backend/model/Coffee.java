package com.coffeeshop.coffeeshop_order_backend.model;

import com.coffeeshop.coffeeshop_order_backend.model.enums.CoffeeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@DiscriminatorValue("COFFEE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Coffee extends Product {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The type of coffee is required")
    private CoffeeType coffeeType;

    @Builder.Default
    private boolean hasMilk = true;

    @Builder.Default
    private boolean hasSugar = true;

}
