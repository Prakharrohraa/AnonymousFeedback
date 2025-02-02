package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.Entity.User;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.dto.LoginRequest;
import com.PrakharRohra.AnonymousFeedback.dto.RegisterRequest;
import com.PrakharRohra.AnonymousFeedback.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDAO userDAO;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserDAO userDAO) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDAO = userDAO;
    }

    public String login(LoginRequest loginRequest) {
        // Fetch all users and find the matching one (since UserDAO lacks a direct find-by-username method)
        List<User> users = userDAO.getAllUsers();
        User user = users.stream()
                .filter(u -> u.getEmail().equals(loginRequest.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password (ensure to hash passwords in production)
        if (user.getPassword().equals(loginRequest.getPassword())) {
            return jwtTokenProvider.generateToken(user);  // Generate token inside AuthService
        }

        throw new RuntimeException("Invalid credentials");
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        List<User> users = userDAO.getAllUsers();
        boolean exists = users.stream().anyMatch(u -> u.getEmail().equals(registerRequest.getEmail()));

        if (exists) {
            throw new ResponseStatusException(409,"User already exists",null);
        }
        boolean isEmailValid = registerRequest.getEmail().contains("@ongrid.in");
        if (!isEmailValid) {
            throw new ResponseStatusException(401,"Email address not valid",null);
        }

        System.out.println("Registering user with email: " + registerRequest.getEmail());

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        newUser.onCreate();

        userDAO.create(newUser);
    }


    // Logic for logout (invalidate the token)
    public void logout(String token) {
        jwtTokenProvider.invalidateToken(token);
    }
}
