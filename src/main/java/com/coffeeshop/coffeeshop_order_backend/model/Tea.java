package com.coffeeshop.coffeeshop_order_backend.model;

import com.coffeeshop.coffeeshop_order_backend.model.enums.TeaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@DiscriminatorValue("TEA")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Tea extends Product {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The type of tea is required")
    private TeaType teaType;
    
    @Builder.Default
    private boolean hasHoney = false;
    
    @Builder.Default
    private boolean hasLemon = false;

}
