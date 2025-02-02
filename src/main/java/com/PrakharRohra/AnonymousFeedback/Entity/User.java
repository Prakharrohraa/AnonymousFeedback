package com.PrakharRohra.AnonymousFeedback.Entity;

import com.PrakharRohra.AnonymousFeedback.ENUM.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "User") // Avoid using reserved keywords
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email; // Used as the username for authentication
    @Column(nullable = true, unique = true)
    private String name="xyz";

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password; // Hashed password for authentication

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Auto-set timestamps
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
