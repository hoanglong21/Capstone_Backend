package com.capstone.project.controller;

import com.capstone.project.service.FeedbackTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class FeedbackTypeController {
    @Autowired
    private FeedbackTypeService feedbackTypeService;

    @GetMapping("/feedbacktype")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(feedbackTypeService.getAll());
    }
}
