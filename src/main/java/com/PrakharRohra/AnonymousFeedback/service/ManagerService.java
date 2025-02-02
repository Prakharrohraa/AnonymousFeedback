package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.Entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final FeedbackDAO feedbackDAO;

    public ManagerService(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    // Fetch all feedback received by the manager's subordinates
    public List<ReviewDTO> getSubordinateReviews(int managerId) {
        List<Feedback> feedbackList = feedbackDAO.findReviewsByManagerId(managerId);
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    // Convert Feedback entity to ReviewDTO
    private ReviewDTO convertToReviewDTO(Feedback feedback) {
        ReviewDTO dto = new ReviewDTO();
        dto.setSubordinateId(feedback.getReceiver().getId()); // Set receiver (subordinate) ID
        dto.setMessage(feedback.getMessage());
        dto.setStatus(feedback.getStatus().name()); // Convert Enum to String
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }

}
