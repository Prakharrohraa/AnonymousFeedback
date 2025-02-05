package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.model.dto.UserDTO;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;
import com.PrakharRohra.AnonymousFeedback.util.PasswordValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserDaoImpl implements UserDAO{
    private final PasswordValidator passwordValidator;
    private EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager, PasswordValidator passwordValidator){
        this.entityManager = entityManager;
        this.passwordValidator = passwordValidator;
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
    public void update(UserDTO user, User tempUser,boolean isHR) {
//        tempUser.setEmail(user.getEmail());
        tempUser.setName(user.getName());
        if(passwordValidator.isValidPassword(user.getPassword())){
            tempUser.setPassword(user.getPassword());
        }
        if(isHR)
        tempUser.setRole(user.getRole());
        if(isHR)
        tempUser.setManager(user.getManager());

//        tempUser.setId(user.getId());
        entityManager.merge(tempUser);
        return;
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
        return;
    }

    @Override
    public List<UserResponse> getAll() {
        return entityManager.createQuery(
                "SELECT new com.PrakharRohra.AnonymousFeedback.model.response.UserResponse(u.name, u.email,u.id,u.manager.id) FROM User u ",
                UserResponse.class
        ).getResultList();
    }
    @Override
    public List<User> getAllUsers(){
        return entityManager.createQuery("SELECT u FROM User u").getResultList();
    }

    @Override
    public List<User> findSubordinatesByManagerId(int managerId) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.manager.id = :managerId", User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }
    @Override
    public User getByEmail(String email) {
        String queryStr = "SELECT u FROM User u WHERE u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst().orElse(null); // Return first result or null if not found
    }
    @Override
    public User getById(int id) {
        return entityManager.find(User.class, id);
    }
}
