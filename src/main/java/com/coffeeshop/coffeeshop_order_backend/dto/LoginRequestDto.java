package com.coffeeshop.coffeeshop_order_backend.dto;

import lombok.*;

//@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
