package com.capstone.project.controller;

import com.capstone.project.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/detection/")
public class DetectionController {

    @Autowired
    private DetectionService detectionService;

    @GetMapping("vocabulary")
    public ResponseEntity<?> detectVocabulary(@RequestParam("text") String text) {
        return ResponseEntity.ok(detectionService.detectVocabulary(text));
    }

    @GetMapping("grammar")
    public ResponseEntity<?> detectGrammar(@RequestParam("text") String text,
                                           @RequestParam(value = "to", defaultValue = "English") String to) {
        try {
            return ResponseEntity.ok(detectionService.detectGrammar(text, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("grammarcheck")
    public ResponseEntity<?> grammarCheck(@RequestParam("text") String text) {
        try {
            return ResponseEntity.ok(detectionService.grammarCheck(text));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
