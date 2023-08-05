package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @GetMapping("fields")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(fieldService.getAll());
    }

    @GetMapping("fields/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getById(@RequestParam("id") int id) {
        try {
            return ResponseEntity.ok(fieldService.getFieldById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("fieldbystudyset/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getFieldsByStudySetTypeId(@PathVariable("id") int id) {
        return ResponseEntity.ok(fieldService.getFieldsByStudySetTypeId(id));
    }

}
