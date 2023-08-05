package com.capstone.project.controller;

import com.capstone.project.dto.ProgressUpdateRequest;
import com.capstone.project.dto.UserUpdateRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Progress;
import com.capstone.project.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/progresses")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAllProgresses() {
        return ResponseEntity.ok(progressService.getAllProgresses());
    }

    @PostMapping("/progresses")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createProgress(@RequestBody Progress progress) {
        try {
            return ResponseEntity.ok(progressService.createProgress(progress));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/progresses/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getProgressById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(progressService.getProgressById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/progresses/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateProgress(@PathVariable("id") int id, @RequestBody Progress progressDetails) {
        try {
            return ResponseEntity.ok(progressService.updateProgress(id, progressDetails));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/progresses/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteProgress(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(progressService.deleteProgress(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/scoreprogress")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateScore(@RequestParam("userid") int userId,
                                         @RequestParam("cardid") int cardId,
                                         @RequestParam("score") int score) {
        return ResponseEntity.ok(progressService.updateScore(userId, cardId, score));
    }

    @PutMapping("/customprogress")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> customUpdateProgress(@RequestBody ProgressUpdateRequest update) {
        return ResponseEntity.ok(progressService.customUpdateProgress(update.getUser(),
                update.getCard(), update.isStar(), update.getPicture(), update.getAudio(), update.getNote(), update.getStatus()));
    }

    @GetMapping("/resetprogress")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> resetProgress(@RequestParam("userid") int userId,
                                           @RequestParam("studysetid") int studySetId) {
        return ResponseEntity.ok(progressService.resetProgress(userId, studySetId));
    }

    @GetMapping("/progressbyuserandcard")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getProgressByUserIdAndCardId(@RequestParam("userid") int userId,
                                                          @RequestParam("cardid") int cardId) {
        try {
            return ResponseEntity.ok(progressService.getProgressByUserIdAndCardId(userId, cardId));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}