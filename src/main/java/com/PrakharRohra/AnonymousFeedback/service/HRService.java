package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.Entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.ENUM.Status;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dto.ReviewDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HRService {

    private final FeedbackDAO feedbackDAO;

    public HRService(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    // Fetch all pending reviews for HR (where receiverId = 1 and status = PENDING)
    public List<ReviewDTO> getPendingReviewsForHR() {
        List<Feedback> feedbackList = feedbackDAO.findByReceiverIdAndStatus(1, Status.PENDING);
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    // Approve a review (update receiverId to 1)
    public void approveReview(int reviewId) {
        Feedback feedback = feedbackDAO.find(reviewId);
        if (feedback == null || feedback.getReceiver().getId() != 1 || feedback.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(404, "Pending review not found for HR approval",null);
        }

        feedback.setStatus(Status.APPROVED);  // Change status
        feedbackDAO.update(feedback);
    }

    // Convert Feedback entity to ReviewDTO
    private ReviewDTO convertToReviewDTO(Feedback feedback) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(feedback.getId());  // Set feedback ID
        dto.setSubordinateId(feedback.getReceiver().getId()); // Set receiver (subordinate) ID
        dto.setMessage(feedback.getMessage());
        dto.setStatus(feedback.getStatus().name()); // Convert Enum to String
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }

}
