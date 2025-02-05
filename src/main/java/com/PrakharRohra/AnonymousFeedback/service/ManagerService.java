package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final FeedbackDAO feedbackDAO;
    @Autowired
    private final UserDAO userDAO;

    public ManagerService(FeedbackDAO feedbackDAO, UserDAO userDAO) {
        this.feedbackDAO = feedbackDAO;
        this.userDAO=userDAO;
    }

    // Fetch all feedback received by the manager's subordinates
    public List<ReviewDTO> getSubordinateReviews(int managerId,String requesterEmail) {
        System.out.println(requesterEmail);
        System.out.println(userDAO.getById(managerId).getEmail());
        if(!requesterEmail.equals(userDAO.getById(managerId).getEmail())) {
            throw new IllegalArgumentException("The requester email doesn't match the manager email");
        }
        List<Feedback> feedbackList = feedbackDAO.findReviewsByManagerId(managerId);
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    // Convert Feedback entity to ReviewDTO
    private ReviewDTO convertToReviewDTO(Feedback feedback) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(feedback.getId());
        dto.setReceiverId(feedback.getReceiver().getId()); // Set receiver (subordinate) ID
        dto.setMessage(feedback.getMessage());
        dto.setStatus(feedback.getStatus().name()); // Convert Enum to String
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }

}
