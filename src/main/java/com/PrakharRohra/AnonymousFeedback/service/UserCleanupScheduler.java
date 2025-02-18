package com.PrakharRohra.AnonymousFeedback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserCleanupScheduler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Run every 15 minutes (900,000 milliseconds)
    @Scheduled(fixedRate = 900000)
    public void deleteUnverifiedUsers() {
        try {
            System.out.println("Running scheduled task to delete unverified users...");

            // Fetch the current timestamp
            LocalDateTime now = LocalDateTime.now();

            // SQL query to delete unverified users
            String sql = "DELETE u FROM user u WHERE enabled = false AND verification_expiration < ?";

            // Execute the query
            int deletedCount = jdbcTemplate.update(sql, now);

            System.out.println("Deleted " + deletedCount + " unverified users.");
        } catch (Exception e) {
            System.err.println("Error deleting unverified users: " + e.getMessage());
        }
    }
}