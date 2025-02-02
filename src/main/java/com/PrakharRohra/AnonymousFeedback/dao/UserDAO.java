package com.PrakharRohra.AnonymousFeedback.dao;

import com.PrakharRohra.AnonymousFeedback.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO {
    void create(User user);
    User read(int id);
    void update(User user);
    void delete(int id);
    List<User> getAllUsers();
    List<User> findSubordinatesByManagerId(int managerId);
}
