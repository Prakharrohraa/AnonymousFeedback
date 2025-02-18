package com.PrakharRohra.AnonymousFeedback.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailValidationService {

    private static final String VALIDATION_API_URL = "https://api.emailvalidation.com/v1/validate";
    private static final String API_KEY = "your_api_key_here";

    public boolean validateEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();
        String url = VALIDATION_API_URL + "?email=" + email + "&apikey=" + API_KEY;

        try {
            EmailValidationResponse response = restTemplate.getForObject(url, EmailValidationResponse.class);
            return response != null && response.isValid(); // Assume the response contains a field `isValid`
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate email address", e);
        }
    }

    // Define a class to map the API response
    private static class EmailValidationResponse {
        private boolean isValid;

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }
    }
}