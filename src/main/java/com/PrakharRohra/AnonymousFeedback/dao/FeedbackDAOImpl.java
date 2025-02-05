package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Status;
import com.PrakharRohra.AnonymousFeedback.model.entity.Feedback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Repository
@Transactional
public class FeedbackDAOImpl implements FeedbackDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Feedback feedback) {
        entityManager.merge(feedback); // Using merge() ensures the entity is managed correctly
    }

    @Override
    public Feedback find(int id) {
        return entityManager.find(Feedback.class, id);
    }

    @Override
    public void update(Feedback feedback) {
        entityManager.merge(feedback);
    }

    @Override
    public void delete(int id) {
        Feedback feedback = entityManager.find(Feedback.class, id);
        if (feedback != null) {
            entityManager.remove(feedback);
        }
        else{

            throw new ResponseStatusException(204,"Feedback doesn't exist",null);
        }

    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        return entityManager.createQuery(
                "SELECT new com.PrakharRohra.AnonymousFeedback.model.dto.FeedbackDTO(f.id, f.receiver.id, f.receiver.manager.id,f.message,f.receiver.name) FROM Feedback f",
                FeedbackDTO.class
        ).getResultList();
    }
    @Override
    public List<Feedback> findReviewsByManagerId(int managerId) {
        return entityManager.createQuery(
                        "SELECT f FROM Feedback f WHERE f.receiver.manager.id = :managerId", Feedback.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    @Override
    public List<Feedback> findByReceiverIdAndStatus(int receiverId, Status status) {
        return entityManager.createQuery(
                        "SELECT f FROM Feedback f WHERE f.receiver.id = :receiverId AND f.status = :status", Feedback.class)
                .setParameter("receiverId", receiverId)
                .setParameter("status", status)
                .getResultList();
    }


}
