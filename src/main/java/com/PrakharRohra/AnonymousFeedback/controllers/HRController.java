package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.dto.ReviewDTO;
import com.PrakharRohra.AnonymousFeedback.service.HRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
public class HRController {

    private final HRService hrService;

    public HRController(HRService hrService) {
        this.hrService = hrService;
    }

    // Get all pending reviews for HR
    @GetMapping("/pending-reviews")
    public ResponseEntity<List<ReviewDTO>> getPendingReviews() {
        return ResponseEntity.ok(hrService.getPendingReviewsForHR());
    }

    // Approve a pending review
    @PutMapping("/approve/{reviewId}")
    public ResponseEntity<String> approveReview(@PathVariable int reviewId) {
        hrService.approveReview(reviewId);
        return ResponseEntity.ok("Review approved successfully");
    }
}
