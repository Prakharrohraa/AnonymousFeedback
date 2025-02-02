package com.PrakharRohra.AnonymousFeedback.controller;

import com.PrakharRohra.AnonymousFeedback.dto.ReviewDTO;
import com.PrakharRohra.AnonymousFeedback.service.ManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/{managerId}/subordinates/reviews")
    public List<ReviewDTO> getSubordinateReviews(@PathVariable int managerId) {
        return managerService.getSubordinateReviews(managerId);
    }
}
