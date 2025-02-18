package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.exception.BadRequestException;
import com.PrakharRohra.AnonymousFeedback.exception.ResourceNotFoundException;
import com.PrakharRohra.AnonymousFeedback.exception.UnauthorizedException;
import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.model.dto.UserDTO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;
import com.PrakharRohra.AnonymousFeedback.util.Constants;
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
        User user = entityManager.createQuery(
                "select u from User u where u.id = :id And u.isDeleted = false",
                User.class
        ).setParameter("id", id).getSingleResult();
        if(user == null){
            throw new ResourceNotFoundException(Constants.NOT_EXISTS);
        }
        return user;
    }

    @Override
    public void update(UserDTO user, User tempUser,boolean isHR) {
//        tempUser.setEmail(user.getEmail());
        User userToUpdate = entityManager.find(User.class, tempUser.getId());
        if(userToUpdate == null || userToUpdate.isDeleted()){
            throw new ResourceNotFoundException(Constants.NOT_EXISTS);
        }
        if(user.getName() != null && !user.getName().isEmpty())
        tempUser.setName(user.getName());
        if(user.getPassword()!=null)
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
        User user = entityManager.find(User.class, id);
        user.setDeleted(true);
        return;
    }

    @Override
    public List<UserResponse> getAll() {
        return entityManager.createQuery(
                "SELECT new com.PrakharRohra.AnonymousFeedback.model.response.UserResponse(u.name, u.email,u.id,u.manager.id) FROM User u where u.isDeleted = false and u.enabled = true",
                UserResponse.class
        ).getResultList();
    }
    @Override
    public List<User> getAllUsers(){
        return entityManager.createQuery("SELECT u FROM User u where u.isDeleted = false AND u.enabled = true ").getResultList();
    }

    @Override
    public List<User> findSubordinatesByManagerId(int managerId) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.manager.id = :managerId and u.isDeleted = false", User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }
    @Override
    public User getByEmail(String email) {
        String queryStr = "SELECT u FROM User u WHERE u.email = :email and u.isDeleted = false";
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst().orElse(null); // Return first result or null if not found
    }
    @Override
    public User getById(int id) {
        User user = entityManager.createQuery(
                "Select u from User u where u.id = :id and u.isDeleted = false",
                User.class
        )
                .setParameter("id", id).getSingleResult();
        if(user == null){
            throw new ResourceNotFoundException(Constants.NOT_EXISTS);
        }
        return user;

    }

    @Override
    public UserResponse getUserResponseById(int id) {
        System.out.println(id);
        return entityManager.createQuery(
                "SELECT new com.PrakharRohra.AnonymousFeedback.model.response.UserResponse(u.name, u.email,u.id,u.manager.id) FROM User u WHERE u.id = :id and u.isDeleted = false",
                UserResponse.class
        ).setParameter("id",id)
                .getSingleResult();
    }
    @Override
    public int getCEOId(Role CEO){
        String queryStr = "SELECT u FROM User u WHERE u.role = :CEO and u.isDeleted = false";
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("CEO", CEO);
        return query.getSingleResult().getId();
    }

    @Override
    public boolean isManager(int id) {
        String queryStr = "Select u FROm User u WHERE u.manager.id = :id and u.isDeleted = false";
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("id", id);
        return (query.getResultList().size() > 0);
    }
}
