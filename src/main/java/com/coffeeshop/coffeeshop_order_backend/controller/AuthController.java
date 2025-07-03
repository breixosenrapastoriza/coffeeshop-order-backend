package com.coffeeshop.coffeeshop_order_backend.controller;

import com.coffeeshop.coffeeshop_order_backend.dto.AuthResponseDto;
import com.coffeeshop.coffeeshop_order_backend.dto.ErrorResponseDto;
import com.coffeeshop.coffeeshop_order_backend.dto.LoginRequestDto;
import com.coffeeshop.coffeeshop_order_backend.dto.RegisterRequestDto;
import com.coffeeshop.coffeeshop_order_backend.model.User;
import com.coffeeshop.coffeeshop_order_backend.model.enums.Role;
import com.coffeeshop.coffeeshop_order_backend.service.UserService;
import com.coffeeshop.coffeeshop_order_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            // Buscar el usuario por username
            User user = userService.loadUserByUsername(request.getUsername());
            if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto.builder().message("Invalid credentials").build());
            }

            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(AuthResponseDto.builder().token(token).user(user).role(String.valueOf(user.getRole())).build());

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponseDto.builder().message("Username does not exist").build());
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        // Verificar si el rol es válido
        try {
            Role.valueOf(request.getRole());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder().message("Invalid role").build());
        }

        // Verificar si el usuario ya existe
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto.builder().message("Username already exists").build());
        }

        // Crear y guardar el usuario
        User user = User
                .builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(Role.valueOf(request.getRole()))
                .build();


        userService.save(user);

        String token = jwtUtil.generateToken(user);

        // Devolver la respuesta usando LoginResponseDto
        return ResponseEntity.ok(AuthResponseDto.builder().token(token).user(user).role(String.valueOf(user.getRole())).build());

    }

}