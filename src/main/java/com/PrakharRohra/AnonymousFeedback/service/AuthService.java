package com.PrakharRohra.AnonymousFeedback.service;

import com.PrakharRohra.AnonymousFeedback.exception.BadRequestException;
import com.PrakharRohra.AnonymousFeedback.exception.ResourceNotFoundException;
import com.PrakharRohra.AnonymousFeedback.exception.UnauthorizedException;
import com.PrakharRohra.AnonymousFeedback.model.entity.User;
import com.PrakharRohra.AnonymousFeedback.dao.UserDAO;
import com.PrakharRohra.AnonymousFeedback.model.enums.Role;
import com.PrakharRohra.AnonymousFeedback.model.request.LoginRequest;
import com.PrakharRohra.AnonymousFeedback.model.request.RegisterRequest;
import com.PrakharRohra.AnonymousFeedback.model.dto.VerifyUserDTO;
import com.PrakharRohra.AnonymousFeedback.jwt.JwtTokenProvider;
import com.PrakharRohra.AnonymousFeedback.model.response.LoginResponse;
import com.PrakharRohra.AnonymousFeedback.util.Constants;
import com.PrakharRohra.AnonymousFeedback.util.PasswordValidator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.PrakharRohra.AnonymousFeedback.service.EmailValidationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDAO userDAO;
    private final EmailService emailService;
    private final PasswordValidator passwordValidator;
    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, UserDAO userDAO, EmailService emailService, PasswordValidator passwordValidator) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDAO = userDAO;
        this.emailService = emailService;
        this.passwordValidator = passwordValidator;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Fetch all users and find the matching one (since UserDAO lacks a direct find-by-username method)
        User user = userDAO.getByEmail(loginRequest.getEmail());
        // Validate password, we still have to hash the password
        if(user==null) {
            throw new BadRequestException(Constants.INVALID_CREDENTIALS);
        }
        if(!user.isEnabled()){
            throw new UnauthorizedException(Constants.NOT_VERIFIED);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(loginRequest.getPassword());
        System.out.println(hashedPassword);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(Constants.INVALID_CREDENTIALS);
        }


        String token = jwtTokenProvider.generateToken(user);
        boolean isHR=false;
        boolean isCEO=false;
        int id = user.getId();
        boolean isManager= userDAO.isManager(id);
        System.out.println(id);
        if(user.getRole()==(Role.HR))isHR=true;
        if(user.getRole()==Role.CEO)isCEO=true;

        boolean isVerified = user.isEnabled();
        LoginResponse loginResponse = new LoginResponse(token,isHR,isCEO,isManager,isVerified,id);
        return loginResponse;

    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        List<User> users = userDAO.getAllUsers();
        boolean exists = users.stream().anyMatch(u -> u.getEmail().equals(registerRequest.getEmail()));

        if (exists) {
            throw new BadRequestException(Constants.EMAIL_ALREADY_EXISTS);
        }
        boolean isValidEmail = registerRequest.getEmail().contains("@ongrid.in");
        if (!isValidEmail) {
            throw new UnauthorizedException(Constants.INVALID_EMAIL);
        }
        if(!passwordValidator.isValidPassword(registerRequest.getPassword())){
            throw new UnauthorizedException(Constants.INVALID_CREDENTIALS);
        }
        System.out.println("Registering user with email: " + registerRequest.getEmail());
        User newUser = new User();
        String email = registerRequest.getEmail().toLowerCase();
        String name = "";
        for(int i=0;i<email.length();i++) {
            if (email.charAt(i) == '@') {
                break;
            }else if(email.charAt(i)=='.') {
                name+=' ';
            }
            else if(email.charAt(i)>='0' &&  email.charAt(i)<='9') {
                continue;
            }
            else {
                name += email.charAt(i);
            }
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setName(name);
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        newUser.setVerificationCode(generateVerificationCode());
        newUser.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(newUser);
        newUser.onCreate();
        newUser.setRole(Role.Employee);
        int managerId = userDAO.getCEOId(Role.CEO);
        newUser.setManager(userDAO.getById(managerId));
        userDAO.create(newUser);
    }
    public void verifyUser(VerifyUserDTO input) {
        Optional<User> optionalUser = Optional.ofNullable(userDAO.getByEmail(input.getEmail()));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new BadRequestException(Constants.VERIFICATION_CODE_EXPIRED);
            }
            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userDAO.create(user);
            } else{
                throw new BadRequestException(Constants.VERIFICATION_CODE_INVALID);
            }

        }else{
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = Optional.ofNullable(userDAO.getByEmail(email));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new BadRequestException(Constants.USER_ALREADY_VERIFIED);
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userDAO.create(user);
        }else{
            throw new UnauthorizedException(Constants.INVALID_CREDENTIALS);
        }
    }
    public void sendVerificationEmail(User user) {
        String email = user.getEmail();

        // Step 1: Validate if the email exists
//        if (!isEmailValid(email)) {
//            throw new ResourceNotFoundException(Constants.INVALID_EMAIL);
//        }

        String subject = "Verification Code";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to Anonymous Feedback App!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        // Step 3: Send the email
        try {
            emailService.sendVerificationEmail(email, subject, htmlMessage);
        } catch (MessagingException e) {
            throw new ResourceNotFoundException(Constants.FAILED_TO_MAIL);
        }
    }

    // Helper method to validate email existence
    private boolean isEmailValid(String email) {
        // Use an email validation API or service to check if the email exists
        // Example: Use a third-party API like ZeroBounce, NeverBounce, or MailboxValidator
        System.out.println("isEmailValid: " + email);
        try {
            EmailValidationService validationService = new EmailValidationService();
            boolean flag = validationService.validateEmail(email); // Assume this returns true if the email exists
            return flag;
        } catch (Exception e) {
            throw new ResourceNotFoundException(Constants.INVALID_EMAIL);
        }
    }

    // Logic for logout (invalidate the token)
    public void logout(String token) {
        jwtTokenProvider.invalidateToken(token);
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) +99999;
        return String.valueOf(code);
    }
}
