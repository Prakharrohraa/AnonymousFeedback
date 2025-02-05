package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HRService {

    private final FeedbackDAO feedbackDAO;
    @Autowired
    private final UserDAO userDAO;


    public HRService(FeedbackDAO feedbackDAO,UserDAO userDAO) {
        this.feedbackDAO = feedbackDAO;
        this.userDAO=userDAO;
    }

    // Fetch all pending reviews for HR (where receiverId = 1 and status = PENDING)
    public List<ReviewDTO> getPendingReviewsForHR(String requesterEmail) {
        if(userDAO.getByEmail(requesterEmail).getRole() != Role.HR) {
            throw new ResponseStatusException(400, "HR approval requester not found",null);
        }
        List<Feedback> feedbackList = feedbackDAO.findByReceiverIdAndStatus(1, Status.PENDING);
        //change it so that we dont have to use *
        //pagination
        return feedbackList.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }
    // Approve a review (update receiverId to 1)
    public void approveReview(int reviewId,String requesterEmail) {
        System.out.println(requesterEmail);
        if(!userDAO.getByEmail(requesterEmail).getRole().equals(Role.HR)) {
            throw new ResponseStatusException(400, "HR approval requester not found",null);
        }
        Feedback feedback = feedbackDAO.find(reviewId);
        System.out.println(feedback);
        if (feedback == null ) {
            throw new ResponseStatusException(400, "Pending review not found for HR approval",null);
        }

        feedback.setStatus(Status.APPROVED);  // Change status
        feedbackDAO.update(feedback);
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
