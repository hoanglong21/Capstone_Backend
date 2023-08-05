package com.capstone.project.controller;

import com.capstone.project.dto.StudySetResponse;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.model.Content;
import com.capstone.project.model.StudySet;
import com.capstone.project.service.StudySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class StudySetController {

    private final StudySetService studySetService;

    @Autowired
    public StudySetController(StudySetService studySetService) {
        this.studySetService = studySetService;
    }

    @GetMapping("/studysets")
    public ResponseEntity<?> getAllStudySets() {
        return ResponseEntity.ok(studySetService.getAllStudySets());
    }

    @PostMapping("/studysets")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createStudySet(@RequestBody StudySet studySet) {
        return ResponseEntity.ok(studySetService.createStudySet(studySet));
    }

    @GetMapping("/studysets/{id}")
    public ResponseEntity<?> getStudySetById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(studySetService.getStudySetById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/studysets/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateStudySet(@PathVariable int id, @RequestBody StudySet studySetDetails) {
        try {
            return ResponseEntity.ok(studySetService.updateStudySet(id, studySetDetails));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/studysets/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteStudySet(@PathVariable int id) {
        try {
            studySetService.deleteStudySet(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deletestudysets/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteHardStudySet(@PathVariable int id) {
        try {
            studySetService.deleteHardStudySet(id);
            return ResponseEntity.ok("Deleted forever successfully");
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/checkstudyset/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> checkStudySet(@PathVariable int id) {
        try {
            return ResponseEntity.ok(studySetService.checkBlankCard(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/studysetAuthor/{username}")
    public ResponseEntity<?> getAllStudySetByUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(studySetService.getAllStudySetByUser(username));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filterstudysets")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "deleted", required = false) Boolean isDeleted,
                                           @RequestParam(value = "public", required = false) Boolean isPublic,
                                           @RequestParam(value = "draft", required = false) Boolean isDraft,
                                           @RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author_id", required = false, defaultValue = "0") int authorId,
                                           @RequestParam(value = "author_name", required = false) String authorName,
                                           @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                           @RequestParam(value = "fromdeteted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromDeteted,
                                           @RequestParam(value = "todeteted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toDeteted,
                                           @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                           @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(studySetService.getFilterList(isDeleted, isPublic, isDraft, search, type, authorId, authorName,
                    fromDeteted, toDeteted, fromCreated, toCreated, sortBy, direction, page, size));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }

    @GetMapping("/quiz")
    public ResponseEntity<?> getQuizByStudySetId(@RequestParam(value = "id") int id,
                                                 @RequestParam(value = "type") int[] type,
                                                 @RequestParam(value = "number") int number,
                                                 @RequestParam(value = "userid") int userId,
                                                 @RequestParam(value = "star") boolean star) {
        try {
            return ResponseEntity.ok(studySetService.getQuizByStudySetId(id, type, number, userId, star));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/learn")
    public ResponseEntity<?> getLearningStudySetId(@RequestParam(value = "userid") int userId,
                                                   @RequestParam(value = "studysetid") int studySetId,
                                                   @RequestParam(value = "questiontype") int[] questionType,
                                                   @RequestParam(value = "progresstype") String[] progressType,
                                                   @RequestParam(value = "random") boolean isRandom,
                                                   @RequestParam(value = "star") boolean star) {
        try {
            return ResponseEntity.ok(studySetService.getLearningStudySetId(userId, studySetId, questionType, progressType, isRandom, star));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/countinfoset")
    public ResponseEntity<?> countCardInSet(@RequestParam(value = "userid") int userId,
                                            @RequestParam(value = "studysetid") int studySetId) {
        try {
            return ResponseEntity.ok(studySetService.countCardInSet(studySetId, userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
