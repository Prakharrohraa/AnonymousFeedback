package com.PrakharRohra.AnonymousFeedback.model.dto;

public class FeedbackDTO {
    private int feedbackId;
    private int receiverId;
    private int managerId;
    private String content;
    private String receiverName;
    public FeedbackDTO(int feedbackId, int receiverId, int managerId,String content,String receiverName) {
        this.feedbackId = feedbackId;
        this.receiverId = receiverId;
        this.managerId = managerId;
        this.content = content;
        this.receiverName = receiverName;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}

