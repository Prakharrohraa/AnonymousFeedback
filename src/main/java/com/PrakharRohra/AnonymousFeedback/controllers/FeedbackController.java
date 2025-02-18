package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.model.request.FeedbackRequest;
import com.PrakharRohra.AnonymousFeedback.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        feedbackService.createFeedback(feedbackRequest);
        return ResponseEntity.ok("Feedback submitted successfully with PENDING status.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectFeedback(@PathVariable int id) {
        feedbackService.rejectFeedback(id);
        return ResponseEntity.ok("Feedback rejected successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable int id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

}
