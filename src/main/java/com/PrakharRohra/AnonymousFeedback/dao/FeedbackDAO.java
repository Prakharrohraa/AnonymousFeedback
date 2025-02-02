package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.ENUM.Status;
import com.PrakharRohra.AnonymousFeedback.Entity.Feedback;

import java.util.List;

public interface FeedbackDAO {
    void create(Feedback feedback);
    Feedback find(int id);
    void update(Feedback feedback);
    void delete(int id);
    List<Feedback> getAllFeedbacks();
    List<Feedback> findReviewsByManagerId(int managerId);
    List<Feedback> findByReceiverIdAndStatus(int receiverId, Status status);
}
