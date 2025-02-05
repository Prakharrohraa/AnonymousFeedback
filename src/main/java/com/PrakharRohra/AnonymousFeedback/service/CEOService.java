package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CEOService {

    private final FeedbackDAO feedbackDAO;
    @Autowired
    private final UserDAO userDAO;
    public CEOService(FeedbackDAO feedbackDAO, UserDAO userDAO) {
        this.feedbackDAO = feedbackDAO;
        this.userDAO = userDAO;
    }

    // Fetch all APPROVED feedbacks for the company (CEO)
    public List<ReviewDTO> getApprovedCompanyReviews(String requesterEmail) {
        if(userDAO.getByEmail(requesterEmail).getRole()!= Role.CEO ) {
            throw new IllegalArgumentException("You are not allowed to view approved company reviews");
        }
        List<Feedback> feedbackList = feedbackDAO.findByReceiverIdAndStatus(1, Status.APPROVED);
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    // Convert Feedback entity to ReviewDTO
    private ReviewDTO convertToReviewDTO(Feedback feedback) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(feedback.getId());  // Set feedback ID
        dto.setReceiverId(feedback.getReceiver().getId()); // Set receiver (subordinate) ID
        dto.setMessage(feedback.getMessage());
        dto.setStatus(feedback.getStatus().name()); // Convert Enum to String
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
}
