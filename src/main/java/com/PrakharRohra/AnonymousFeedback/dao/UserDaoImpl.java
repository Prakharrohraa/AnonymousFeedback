package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDAO{
    private EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void create(User user){
        entityManager.persist(user);
    }

    @Override
    public User read(int id){
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
        return;
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
        return;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createNativeQuery("SELECT * FROM User", User.class)
                .getResultList();
    }
    @Override
    public List<User> findSubordinatesByManagerId(int managerId) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.manager.id = :managerId", User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }
}
