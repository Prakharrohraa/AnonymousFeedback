package com.PrakharRohra.AnonymousFeedback.controllers;

import com.PrakharRohra.AnonymousFeedback.model.dto.ReviewDTO;
import com.PrakharRohra.AnonymousFeedback.service.CEOService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CEOController {

    private final CEOService ceoService;

    public CEOController(CEOService ceoService) {
        this.ceoService = ceoService;
    }

    // Get all approved feedbacks for the company (CEO view)
    @GetMapping("/approved-reviews")
    public ResponseEntity<List<ReviewDTO>> getApprovedCompanyReviews( HttpServletRequest request) {
        String requesterEmail = (String) request.getAttribute("requesterEmail"); // Get email from interceptor
        return ResponseEntity.ok(ceoService.getApprovedCompanyReviews(requesterEmail));
    }
}
