package com.ankit.jewellery_billing.controller;

import com.ankit.jewellery_billing.dto.ApiResponse;
import com.ankit.jewellery_billing.dto.LoginRequest;
import com.ankit.jewellery_billing.dto.LoginResponse;
import com.ankit.jewellery_billing.dto.RegisterRequest;
import com.ankit.jewellery_billing.dto.GoogleLoginRequest;
import com.ankit.jewellery_billing.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/google-login")
    public ResponseEntity<ApiResponse<LoginResponse>> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        LoginResponse response = authService.googleLogin(request);
        return ResponseEntity.ok(ApiResponse.success("Google login successful", response));
    }
}
