package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.exception.ResourceNotFoundException;
import com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.dao.FeedbackDAO;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.request.FeedbackRequest;
import com.PrakharRohra.AnonymousFeedback.util.Constants;
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
        User receiver = userDAO.getByEmail(feedbackRequest.getReceiverEmail());
        if (receiver == null) {
            throw new ResourceNotFoundException(Constants.NOT_EXISTS + feedbackRequest.getReceiverEmail());
        }
        if(!receiver.isEnabled()){
            throw new ResourceNotFoundException(Constants.NOT_EXISTS + feedbackRequest.getReceiverEmail());
        }
        Feedback feedback = new Feedback();
        feedback.setReceiver(receiver);
        feedback.setMessage(feedbackRequest.getMessage());
        if(receiver.getId() == userDAO.getCEOId(Role.CEO))
        feedback.setStatus(Status.PENDING);
        else feedback.setStatus(Status.APPROVED);
        feedbackDAO.create(feedback);
    }

    public Feedback getFeedbackById(int id) {
        return feedbackDAO.find(id);
    }

    public List<FeedbackDTO> getAllFeedbacks() {

        return feedbackDAO.getAllFeedbacks();
    }

    public void deleteFeedback(int id) {

        feedbackDAO.delete(id);
    }
    public void rejectFeedback(int id) {
        feedbackDAO.rejectFeedback(id);
    }


}
