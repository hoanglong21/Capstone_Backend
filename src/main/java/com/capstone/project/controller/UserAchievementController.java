package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.UserAchievement;
import com.capstone.project.service.UserAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserAchievementController {
    @Autowired
    private UserAchievementService userAchievementService;

    @GetMapping("/userachievements")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userAchievementService.getAll());
    }

    @PostMapping("/userachievements")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createUserAchievement(@RequestBody UserAchievement userAchievement) {
        try {
            return ResponseEntity.ok(userAchievementService.createUserAchievement(userAchievement));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userachievements/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getUserAchievementById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userAchievementService.getUserAchievementById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userachievementsbyuser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getUserAchievementByUserId(@PathVariable("id") int id) {
        return ResponseEntity.ok(userAchievementService.getUserAchievementByUserId(id));
    }

    @GetMapping("/filteruserachievement")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterUserAchievement(@RequestParam(value = "userid", required = false, defaultValue = "0") int userId,
                                        @RequestParam(value = "achievementid", required = false, defaultValue = "0") int achievementId,
                                       @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                       @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                       @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                        @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
//        try {
        try {
            return ResponseEntity.ok(userAchievementService.filterUserAchievement(userId, achievementId, fromCreated, toCreated,
                    sortBy, direction, page, size));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }
}
