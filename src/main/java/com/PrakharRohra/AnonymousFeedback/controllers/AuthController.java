package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.model.response.JwtResponse;
import com.PrakharRohra.AnonymousFeedback.model.request.LoginRequest;
import com.PrakharRohra.AnonymousFeedback.model.request.RegisterRequest;
import com.PrakharRohra.AnonymousFeedback.model.dto.VerifyUserDTO;
import com.PrakharRohra.AnonymousFeedback.model.response.LoginResponse;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;
import com.PrakharRohra.AnonymousFeedback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin("http://localhost:5173")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Delegate the login logic to AuthService
        LoginResponse token = authService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Delegate the registration logic to AuthService
        authService.register(registerRequest);
        return ResponseEntity.status(202).body("Verify Now using otp");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDto) {
        try {
            authService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Delegate the logout logic to AuthService
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email"); // Extract email from request
        System.out.println("Email received for OTP resend: " + email); // Debugging log
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("OTP resent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}