package com.coffeeshop.coffeeshop_order_backend.dto;

import com.coffeeshop.coffeeshop_order_backend.model.User;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private User user;
    private String role;
}
