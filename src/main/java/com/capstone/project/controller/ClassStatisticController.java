package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.ClassStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/statistic")
public class ClassStatisticController {

    @Autowired
    private ClassStatisticService classStatisticService;

    @GetMapping("/classtest/{id}")
    public ResponseEntity<?> getTestNumber(@PathVariable int id){
        try {
            return ResponseEntity.ok(classStatisticService.getTestNumber(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/classassignment/{id}")
    public ResponseEntity<?> getAssignmentNumber(@PathVariable int id){
        try {
            return ResponseEntity.ok(classStatisticService.getAssignmentNumber(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/leanerjoinednum/{id}")
    public ResponseEntity<?> getLeanerJoinedNumber(@PathVariable int id){
        try {
            return ResponseEntity.ok(classStatisticService.getLeanerJoinedNumber(id));
        } catch (ResourceNotFroundException | ParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/learnerjoinedgrowth/{id}")
    public ResponseEntity<?> getLeanerJoinedGrowth(@PathVariable int id){
        try {
            return ResponseEntity.ok(classStatisticService.getLeanerJoinedGrowth(id));
        } catch (ResourceNotFroundException | ParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/postgrowth/{id}")
    public ResponseEntity<?> getPostGrowth(@PathVariable int id) {
        try {
            return ResponseEntity.ok(classStatisticService.getPostGrowth(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pointdistribution/{id}")
    public ResponseEntity<?> getPointDistribution(@PathVariable int id){
        try {
            return ResponseEntity.ok(classStatisticService.getPointDistribution(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
