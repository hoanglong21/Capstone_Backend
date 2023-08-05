package com.capstone.project.controller;

import com.capstone.project.service.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/translate/")
public class TranslateController {
    @Autowired
    private TranslateService translateService;

    @GetMapping("clients5")
    public ResponseEntity<?> translateClients5(@RequestParam("text") String text, @RequestParam("to") String to) {
        try {
            return ResponseEntity.ok(translateService.translateClients5(text, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("googleapis")
    public ResponseEntity<?> translateGoogleapis(@RequestParam("text") String text, @RequestParam("to") String to) {
        try {
            return ResponseEntity.ok(translateService.translateGoogleapis(text, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("mymemory")
    public ResponseEntity<?> translateMymemory(@RequestParam("text") String text, @RequestParam("to") String to) {
        try {
            return ResponseEntity.ok(translateService.translateMymemory(text, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
