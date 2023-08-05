package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/cardbystudysetid")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getAllByStudySetId(@RequestParam int id) {
        return ResponseEntity.ok(cardService.getAllByStudySetId(id));
    }

    @PostMapping("/cards")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createCard(@RequestBody Card card) {
        return ResponseEntity.ok(cardService.createCard(card));
    }

    @GetMapping("/cards/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getCardById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(cardService.getCardById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cards/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateCard(@PathVariable int id, @RequestBody Card cardDetails) {
        try {
            return ResponseEntity.ok(cardService.updateCard(id, cardDetails));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cards/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteCard(@PathVariable int id) {
        try {
            return ResponseEntity.ok(cardService.deleteCard(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtercard")
    public ResponseEntity<?> getFilterCard(@RequestParam(value = "userid") int userId,
                                           @RequestParam(value = "studysetid") int studySetId,
                                           @RequestParam(value = "status") String[] status,
                                           @RequestParam(value = "star") boolean star,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "status") String sortBy,
                                           @RequestParam(value = "direction", required = false, defaultValue = "ASC") String direction,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(cardService.getFilterCard(studySetId, userId, status, star, sortBy, direction, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
