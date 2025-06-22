package com.coffeeshop.coffeeshop_order_backend.controller;

import com.coffeeshop.coffeeshop_order_backend.dto.AuthResponseDto;
import com.coffeeshop.coffeeshop_order_backend.dto.LoginRequestDto;
import com.coffeeshop.coffeeshop_order_backend.dto.RegisterRequestDto;
import com.coffeeshop.coffeeshop_order_backend.model.User;
import com.coffeeshop.coffeeshop_order_backend.model.enums.Role;
import com.coffeeshop.coffeeshop_order_backend.service.UserService;
import com.coffeeshop.coffeeshop_order_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        // Buscar el usuario por username
        User user = userService.loadUserByUsername(request.getUsername());

        // Verificar la contraseña
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        System.out.println("Alert");
        return ResponseEntity.ok(new AuthResponseDto(token, user, user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        // Verificar si el usuario ya existe
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Crear y guardar el usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // La contraseña se encriptará en el UserService
        user.setRole(Role.valueOf(request.getRole()));

        userService.saveUser(user);

        // Generar token JWT
        String token = jwtUtil.generateToken(user);

        // Devolver la respuesta usando LoginResponseDto
        return ResponseEntity.ok(new AuthResponseDto(token, user, user.getRole().toString()));

    }

}