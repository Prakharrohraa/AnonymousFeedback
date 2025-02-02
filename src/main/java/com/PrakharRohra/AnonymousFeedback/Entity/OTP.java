package com.PrakharRohra.AnonymousFeedback.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private String otp;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt = createdAt.plus(10,ChronoUnit.MINUTES);
}
