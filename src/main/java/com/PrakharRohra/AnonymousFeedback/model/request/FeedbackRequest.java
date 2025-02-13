package com.PrakharRohra.AnonymousFeedback.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private String receiverEmail;
    private String message;

}
