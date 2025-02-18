package com.PrakharRohra.AnonymousFeedback.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public interface Constants {
     String INVALID_EMAIL = "Invalid email, PLease make sure that email is correct and ensure that email belongs to Ongrid";
     String EMAIL_ALREADY_EXISTS = "Email already exists";
     String INVALID_CREDENTIALS = "Email or Password is incorrect";
     String NOT_VERIFIED = "Not verified, Please verify your email";
     String FAILED_TO_MAIL = "Failed to send email";
     String USER_ALREADY_VERIFIED = "User already verified";
     String VERIFICATION_CODE_EXPIRED = "Verification code expired";
     String VERIFICATION_CODE_INVALID = "Verification code is invalid, Please enter correct verification code";

     String NOT_A_CEO = "You are not allowed to view company reviews";
     String NOT_A_HR = "This page is only allowed to viewed by HR";
     String NOT_EXISTS = "User does not exist with address : ";
     String NOT_CORRECT_MANAGER = "Requester doens't match with the requesting manager";
     String FEEDBACK_DOESNT_EXISTS = "Feedback does not exist";
}
