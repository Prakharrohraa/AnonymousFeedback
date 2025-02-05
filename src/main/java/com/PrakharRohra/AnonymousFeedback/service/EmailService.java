package com.PrakharRohra.AnonymousFeedback.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static java.awt.SystemColor.text;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage Message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(Message,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(Message);
    }
}
