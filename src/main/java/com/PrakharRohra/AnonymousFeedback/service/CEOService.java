package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.Entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.ENUM.Status;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CEOService {

    private final FeedbackDAO feedbackDAO;

    public CEOService(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    // Fetch all APPROVED feedbacks for the company (CEO)
    public List<ReviewDTO> getApprovedCompanyReviews() {
        List<Feedback> feedbackList = feedbackDAO.findByReceiverIdAndStatus(1, Status.APPROVED);
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
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
