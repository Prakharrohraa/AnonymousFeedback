package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;

import java.util.List;

public interface FeedbackDAO {
    void create(Feedback feedback);
    Feedback find(int id);
    void update(Feedback feedback);
    void delete(int id);
    List<FeedbackDTO> getAllFeedbacks();
    List<Feedback> findReviewsByManagerId(int managerId);
    List<Feedback> findByReceiverIdAndStatus(int receiverId, Status status);
}
