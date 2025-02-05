package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.model.dto.UserDTO;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;
import com.PrakharRohra.AnonymousFeedback.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/getall")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody UserDTO user, HttpServletRequest request) {
        String requesterEmail = (String) request.getAttribute("requesterEmail"); // Get email from interceptor
        if (requesterEmail == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        User updatedUser = userService.updateUser(id, user, requesterEmail);
        return ResponseEntity.ok(updatedUser);
    }


}
