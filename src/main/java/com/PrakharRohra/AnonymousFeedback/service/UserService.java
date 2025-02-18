package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.exception.BadRequestException;
import com.PrakharRohra.AnonymousFeedback.exception.UnauthorizedException;
import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.dto.UserDTO;
import com.PrakharRohra.AnonymousFeedback.model.response.UserResponse;
import com.PrakharRohra.AnonymousFeedback.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;
    @Autowired
    UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public List<UserResponse> getAll() {
        return userDAO.getAll();
    }
    public User updateUser(int id, UserDTO user, String requesterEmail) {
        User tempUser = userDAO.getById(id);
        if (tempUser == null) {
            throw new BadRequestException(Constants.NOT_EXISTS);
        }
        System.out.println(requesterEmail);
        System.out.println(user.getEmail());
        System.out.println(user.getEmail());
        // Get the requesting user from the database
        User requester = userDAO.getByEmail(requesterEmail);
        if (requester == null) {
            throw new UnauthorizedException();
        }
        // Ensure that only the Head of HR or the user themselves can update
        if (!requester.getRole().equals("HR") && !(tempUser.getEmail().equals(requesterEmail))) {
            throw new UnauthorizedException();
        }

        // Prevent changing ID
//        if(!requesterEmail.equals(tempUser.getEmail())) {
//            throw new IllegalArgumentException("User not authorized");
//        }
        // Update the user details
//        tempUser.setPassword(user.getPassword());
//        tempUser.setEmail(user.getEmail());
//        tempUser.setName(user.getName());
//        tempUser.setRole(user.getRole());
        userDAO.update(user,tempUser,requester.getRole().equals("HR"));
        return tempUser;
    }
    public UserResponse getUserResponseById(int id,String requesterEmail) {
        if(!userDAO.getById(id).getEmail().equals(requesterEmail)) {
            throw new UnauthorizedException();
        }
        return userDAO.getUserResponseById(id);
    }

}
