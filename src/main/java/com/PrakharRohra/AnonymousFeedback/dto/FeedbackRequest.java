package com.PrakharRohra.AnonymousFeedback.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private int receiverId;
    private String message;

}
