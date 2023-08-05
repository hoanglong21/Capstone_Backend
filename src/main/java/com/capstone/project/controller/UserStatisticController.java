package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.UserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/statistic")
public class UserStatisticController {

    @Autowired
    private UserStatisticService userStatisticService;

    @GetMapping("/user/access/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAccessStatistic(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userStatisticService.getAccessStatistic(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/studysetlearned/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getStudySetLearnedStatistic(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userStatisticService.getStudySetLearnedStatistic(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/classjoined/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getClassJoinedStatistic(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userStatisticService.getClassJoinedStatistic(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/learning/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getLearningStatistic(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userStatisticService.getLearningStatistic(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
