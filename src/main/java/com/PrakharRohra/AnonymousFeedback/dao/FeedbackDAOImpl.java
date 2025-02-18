package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.exception.ResourceNotFoundException;
import com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import com.PrakharRohra.AnonymousFeedback.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
@Transactional
public class FeedbackDAOImpl implements FeedbackDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDAO userDAO;
    @Override
    public void create(Feedback feedback) {
        entityManager.merge(feedback); // Using merge() ensures the entity is managed correctly
    }

    @Override
    public Feedback find(int id) {
        Feedback feedback = entityManager.createQuery(
                "select  f from Feedback  f where f.id = :id and f.isDeleted =false",
                Feedback.class
        ).setParameter("id", id).getSingleResult();
        if(feedback == null){
            throw new ResourceNotFoundException(Constants.FEEDBACK_DOESNT_EXISTS);
        }
        return feedback;
    }

    @Override
    public void update(Feedback feedback) {
        Feedback f = entityManager.find(Feedback.class, feedback.getId());
        if(f.isDeleted()){
            throw new ResourceNotFoundException(Constants.FEEDBACK_DOESNT_EXISTS);
        }
        entityManager.merge(feedback);
    }

    @Override
    public void delete(int id) {
        Feedback feedback = entityManager.find(Feedback.class, id);
        if (feedback != null) {
            feedback.setDeleted(true);
        }
        else{
            throw new ResourceNotFoundException(Constants.FEEDBACK_DOESNT_EXISTS);
        }
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        Role C = Role.CEO;
        return entityManager.createQuery(
                "SELECT new com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO(f.id, f.receiver.id, f.receiver.manager.id,f.message,f.receiver.name) FROM Feedback f where NOT f.receiver.role=:C and f.isDeleted=false",
                FeedbackDTO.class
        ).setParameter("C", C).getResultList();
    }
    @Override
    public List<Feedback> findReviewsByManagerId(int managerId) {
        return entityManager.createQuery(
                        "SELECT f FROM Feedback f WHERE f.receiver.manager.id = :managerId and f.isDeleted = false", Feedback.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    @Override
    public List<Feedback> findByReceiverIdAndStatus(int receiverId, Status status) {
        return entityManager.createQuery(
                        "SELECT f FROM Feedback f WHERE f.receiver.id = :receiverId AND f.status = :status and f.isDeleted = false", Feedback.class)
                .setParameter("receiverId", receiverId)
                .setParameter("status", status)
                .getResultList();
    }
    @Override
    public void rejectFeedback(int feedbackId){
        Feedback f = entityManager.createQuery(
                "Select f FROM Feedback f WHERE f.id = :feedbackId  and f.isDeleted = false",Feedback.class
        ).setParameter("feedbackId", feedbackId).getSingleResult();
        if(f != null){
            f.setStatus(Status.REJECTED);
            return;
        }
        throw new ResourceNotFoundException(Constants.FEEDBACK_DOESNT_EXISTS);
    }


}
