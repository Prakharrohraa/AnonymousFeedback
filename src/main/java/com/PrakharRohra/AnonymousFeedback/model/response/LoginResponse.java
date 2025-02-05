package com.PrakharRohra.AnonymousFeedback.model.response;

public class LoginResponse {
    private String token;
    private boolean isHR;
    private boolean isCEO;
    private boolean isManager;

    public LoginResponse(String token, boolean isHR, boolean isCEO, boolean isManager) {
        this.token = token;
        this.isHR = isHR;
        this.isCEO = isCEO;
        this.isManager = isManager;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isHR() {
        return isHR;
    }

    public void setHR(boolean HR) {
        isHR = HR;
    }

    public boolean isCEO() {
        return isCEO;
    }

    public void setCEO(boolean CEO) {
        isCEO = CEO;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
