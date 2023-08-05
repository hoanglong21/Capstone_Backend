package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Feedback;
import com.capstone.project.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedbacks")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @PostMapping("/feedbacks")
    public ResponseEntity<?> createFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedback));
    }

    @GetMapping("/feedbacks/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getFeedbackById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(feedbackService.getFeedbackById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/feedbacks/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateFeedback(@PathVariable("id") int id, @RequestBody Feedback feedbackDetails) {
        try {
            return ResponseEntity.ok(feedbackService.updateFeedback(id, feedbackDetails));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/feedbacks/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteFeedback(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(feedbackService.deleteFeedback(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filterfeedback")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterFeedback(@RequestParam(value = "search", required = false, defaultValue = "") String search,
                                        @RequestParam(value = "author_id", required = false, defaultValue = "0") int authorId,
                                        @RequestParam(value = "author_name", required = false) String authorName,
                                        @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                        @RequestParam(value = "destination", required = false) String destination,
                                        @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                        @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                        @RequestParam(value = "sortby", required = false) String sortBy,
                                        @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(feedbackService.filterFeedback(search, type, authorId, authorName, destination, fromCreated, toCreated, sortBy, direction, page, size));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }

    @GetMapping("/replyfeedback")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> replyFeedback(@RequestParam("id") int id,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content) {
        try {
            return ResponseEntity.ok(feedbackService.replyFeedback(id, title, content));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
