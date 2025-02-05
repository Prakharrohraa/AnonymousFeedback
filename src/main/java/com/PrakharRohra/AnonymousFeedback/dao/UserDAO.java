package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.model.dto.UserDTO;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;

import java.util.List;

public interface UserDAO {
    void create(User user);
    User read(int id);
    void update(UserDTO user,User tempUser,boolean isHR);


    void delete(int id);
    List<User> getAllUsers();

    List<UserResponse> getAll();

    List<User> findSubordinatesByManagerId(int managerId);

    User getByEmail(String email);

    User getById(int id);
}
