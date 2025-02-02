package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.dto.JwtResponse;
import com.PrakharRohra.AnonymousFeedback.dto.LoginRequest;
import com.PrakharRohra.AnonymousFeedback.dto.RegisterRequest;
import com.PrakharRohra.AnonymousFeedback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Delegate the login logic to AuthService
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Delegate the registration logic to AuthService
        authService.register(registerRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Delegate the logout logic to AuthService
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }

}