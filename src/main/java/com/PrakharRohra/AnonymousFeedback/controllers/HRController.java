package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.model.dto.ReviewDTO;
import com.PrakharRohra.AnonymousFeedback.service.HRService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<ReviewDTO>> getPendingReviews(HttpServletRequest request) {
        String requesterEmail = (String) request.getAttribute("requesterEmail"); // Get email from interceptor
        return ResponseEntity.ok(hrService.getPendingReviewsForHR(requesterEmail));
    }

    // Approve a pending review
    @PutMapping("/approve/{reviewId}")
    public ResponseEntity<String> approveReview(@PathVariable int reviewId , HttpServletRequest request) {
        String requesterEmail = (String) request.getAttribute("requesterEmail"); // Get email from interceptor
        hrService.approveReview(reviewId,requesterEmail);
        return ResponseEntity.ok("Review approved successfully");
    }
}
