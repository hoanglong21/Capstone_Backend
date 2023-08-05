package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.ClassLearner;
import com.capstone.project.model.UserAchievement;
import com.capstone.project.service.ClassLearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class ClassLeanerController {

    @Autowired
    private ClassLearnerService classLearnerService;

    @GetMapping("/classleaner")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(classLearnerService.getAll());
    }

    @PostMapping ("/classleaner")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createClassLeaner(@RequestBody ClassLearner classLearner) {
        try {
            return ResponseEntity.ok(classLearnerService.createClassLearner(classLearner));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping ("/classleaner/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateClassLeaner(@RequestBody ClassLearner classLearner, @PathVariable int id) {
        try {
            return ResponseEntity.ok(classLearnerService.updateClassLearner(classLearner,id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/classleaner/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getClassLeanerById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(classLearnerService.getClassLeanerById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/classleanerbyuser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getClassLeanerByUserId(@PathVariable("id") int id) {
        return ResponseEntity.ok(classLearnerService.getClassLeanerByUserId(id));
    }

    @DeleteMapping("/delclasslearner")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteClassLearner(@RequestParam(value = "userid", required = false, defaultValue = "0") int userid,
                                                @RequestParam(value = "classid", required = false, defaultValue = "0") int classid) {
        try {
            return ResponseEntity.ok(classLearnerService.deleteClassLearner(userid,classid));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delclasslearnerbyid/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteClassLearnerById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(classLearnerService.deleteClassLearnerById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/filterclasslearner")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterClassLeaner(@RequestParam(value = "userid", required = false, defaultValue = "0") int userId,
                                                   @RequestParam(value = "classid", required = false, defaultValue = "0") int classId,
                                                   @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                                   @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                                   @RequestParam(value = "accepted", required = false) Boolean isAccepted,
                                                   @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                                   @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(classLearnerService.filterClassLearner(userId, classId, fromCreated, toCreated,isAccepted,
                    sortBy, direction, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }


    @GetMapping("/filtergetlearner")
//    @PreAuthorize(" hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterGetLeaner(@RequestParam(value = "userid", required = false, defaultValue = "0") int userId,
                                               @RequestParam(value = "classid", required = false, defaultValue = "0") int classId,
                                               @RequestParam(value = "accepted", required = false) Boolean isAccepted,
                                               @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                               @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                               @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(classLearnerService.filterGetLearner(userId, classId, isAccepted,
                    sortBy, direction, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }
}
