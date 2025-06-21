package com.coffeeshop.coffeeshop_order_backend.dto;

import com.coffeeshop.coffeeshop_order_backend.model.User;
import lombok.*;

//@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private User user;
    private String role;
}
