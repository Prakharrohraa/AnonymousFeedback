package com.PrakharRohra.AnonymousFeedback.model.response;

import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.model.enums.Role;

public class UserResponse {
    private String name;
    private String email;
    private int id;
    private int managerId;

    public UserResponse(String name, String email, int id, int managerId) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }
    public int getManager() {
        return managerId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
