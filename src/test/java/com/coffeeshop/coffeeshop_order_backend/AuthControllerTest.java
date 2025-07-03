package com.coffeeshop.coffeeshop_order_backend;

import com.coffeeshop.coffeeshop_order_backend.dto.LoginRequestDto;
import com.coffeeshop.coffeeshop_order_backend.dto.RegisterRequestDto;
import com.coffeeshop.coffeeshop_order_backend.model.User;
import com.coffeeshop.coffeeshop_order_backend.model.enums.Role;
import com.coffeeshop.coffeeshop_order_backend.security.JwtUtil;
import com.coffeeshop.coffeeshop_order_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private LoginRequestDto loginRequest;
    private RegisterRequestDto registerRequest;
    private RegisterRequestDto invalidRegisterRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("hashedpassword") // In a real scenario, this would be hashed
                .role(Role.ROLE_USER)
                .build();

        loginRequest = LoginRequestDto.builder()
                .username("testuser")
                .password("password123")
                .build();

        registerRequest = RegisterRequestDto.builder()
                .username("newuser")
                .password("password123")
                .role("ROLE_USER").build();

        invalidRegisterRequest = RegisterRequestDto.builder()
                .username("newuser")
                .password("password123")
                .role("INVALID_ROLE")
                .build();
    }

    @Test
    void login_ValidCredentials_ShouldReturnTokenAndUser() throws Exception {
        // Arrange
        when(userService.loadUserByUsername(anyString())).thenReturn(user);
        when(userService.existsByUsername(anyString())).thenReturn(true);
        when(userService.checkPassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("test.jwt.token");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username").value(user.getUsername()))
                .andExpect(jsonPath("$.user.role").value(user.getRole().name()));

        verify(userService, times(1)).loadUserByUsername(loginRequest.getUsername());
        verify(userService, times(1)).checkPassword(anyString(), anyString());
    }

    @Test
    void login_UserNotFound_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        when(userService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));
        when(userService.existsByUsername(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).loadUserByUsername(loginRequest.getUsername());
    }

    @Test
    void login_InvalidPassword_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        when(userService.loadUserByUsername(anyString())).thenReturn(user);
        when(userService.existsByUsername(anyString())).thenReturn(true);
        when(userService.checkPassword(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).checkPassword(anyString(), anyString());
    }

    @Test
    void register_NewUser_ShouldReturnTokenAndUser() throws Exception {
        // Arrange
        when(userService.existsByUsername(anyString())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("test.jwt.token");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username").value(registerRequest.getUsername()))
                .andExpect(jsonPath("$.user.role").value(registerRequest.getRole()));

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void register_ExistingUsername_ShouldReturnConflict() throws Exception {
        // Arrange
        when(userService.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());

        verify(userService, never()).save(any(User.class));
    }

    @Test
    void register_InvalidRole_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(userService.existsByUsername(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).save(any(User.class));
    }
}