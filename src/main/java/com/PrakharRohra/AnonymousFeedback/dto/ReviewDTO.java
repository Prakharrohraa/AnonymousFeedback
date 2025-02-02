package com.PrakharRohra.AnonymousFeedback.dto;

import com.PrakharRohra.AnonymousFeedback.ENUM.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {

    private int id;
    private int subordinateId;            // Feedback ID
    private String Message;      // Feedback content
    private LocalDateTime createdAt; // When the feedback was created
    private LocalDateTime updatedAt; // When the feedback was updated
    private String status = Status.PENDING.toString();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubordinateId() {
        return subordinateId;
    }

    public void setSubordinateId(int subordinateId) {
        this.subordinateId = subordinateId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // Add any other necessary fields that you want the manager to see
}
