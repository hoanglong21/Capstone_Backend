package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.History;
import com.capstone.project.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @GetMapping("/histories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(historyService.getAll());
    }

    @GetMapping("/histories/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getHistoryById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(historyService.getHistoryById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/histories")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> createHistory(@RequestBody History history) {
        return ResponseEntity.ok(historyService.createHistory(history));
    }

    @GetMapping("/filterhistory")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterHistory(@RequestParam(value = "userid", required = false, defaultValue = "0") int userId,
                                        @RequestParam(value = "destinationid", required = false, defaultValue = "0") int destinationId,
                                        @RequestParam(value = "typeid", required = false, defaultValue = "1") int typeId,
                                        @RequestParam(value = "categoryId", required = false, defaultValue = "0") int categoryId,
                                        @RequestParam(value = "fromdatetime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromDatetime,
                                        @RequestParam(value = "todatetime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toDatetime,
                                        @RequestParam(value = "sortby", required = false, defaultValue = "datetime") String sortBy,
                                        @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(historyService.filterHistory(userId, destinationId, typeId, categoryId, fromDatetime, toDatetime,
                    sortBy, direction, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }
}
