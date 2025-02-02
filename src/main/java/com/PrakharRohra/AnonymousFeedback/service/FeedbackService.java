package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.ENUM.Status;
import com.PrakharRohra.AnonymousFeedback.Entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.Entity.User;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.dto.FeedbackRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackDAO feedbackDAO;
    private final UserDAO userDAO;

    public FeedbackService(FeedbackDAO feedbackDAO, UserDAO userDAO) {
        this.feedbackDAO = feedbackDAO;
        this.userDAO = userDAO;
    }

    public void createFeedback(FeedbackRequest feedbackRequest) {
        User receiver = userDAO.read(feedbackRequest.getReceiverId());
        if (receiver == null) {
            throw new RuntimeException("Receiver not found with ID: " + feedbackRequest.getReceiverId());
        }

        Feedback feedback = new Feedback();
        feedback.setReceiver(receiver);
        feedback.setMessage(feedbackRequest.getMessage());
        feedback.setStatus(Status.PENDING);
        feedbackDAO.create(feedback);
    }

    public Feedback getFeedbackById(int id) {
        return feedbackDAO.find(id);
    }

    public List<Feedback> getAllFeedbacks() {

        return feedbackDAO.getAllFeedbacks();
    }

    public void deleteFeedback(int id) {

        feedbackDAO.delete(id);
    }
}
