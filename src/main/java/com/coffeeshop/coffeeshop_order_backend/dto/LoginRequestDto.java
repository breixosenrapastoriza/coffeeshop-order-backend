package com.coffeeshop.coffeeshop_order_backend.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
