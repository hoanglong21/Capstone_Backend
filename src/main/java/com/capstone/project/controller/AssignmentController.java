package com.capstone.project.controller;

import com.capstone.project.dto.AssignmentRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Assignment;
import com.capstone.project.model.Card;
import com.capstone.project.service.AssignmentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class AssignmentController {

    @Autowired
    private ModelMapper modelMapper;
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }


    @GetMapping("/assignments")
    public ResponseEntity<?> getAllAssignment() {
        return ResponseEntity.ok(assignmentService.getAllAssignment());
    }
    @GetMapping("/assignmentsbyclassid/{id}")
    public ResponseEntity<?> getAllAssignmentByClassId(@PathVariable int id) {
        return ResponseEntity.ok(assignmentService.getAllAssignmentByClassId(id));
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(assignmentService.getAssignmentById(id));
        } catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@Valid @RequestBody AssignmentRequest assignmentRequest, BindingResult result){
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
               Assignment assignment = modelMapper.map(assignmentRequest,Assignment.class);
            try {
                return ResponseEntity.ok(assignmentService.createAssignment(assignment));
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }


//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @PutMapping ("/assignments/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable int id, @Valid @RequestBody AssignmentRequest assignmentRequest,
                                              BindingResult result){
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Assignment assignment = modelMapper.map(assignmentRequest,Assignment.class);
            try {
                return ResponseEntity.ok(assignmentService.updateAssignment(id, assignment));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    //    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable int id) {
        try {
            return ResponseEntity.ok(assignmentService.deleteAssignment(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filterassignment")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "fromstarted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromStart,
                                           @RequestParam(value = "tostarted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toStart,
                                           @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                           @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                           @RequestParam(value = "draft", required = false) Boolean isDraft,
                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                           @RequestParam(value = "classid", required = false) Optional<Integer> classid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        try{
            return ResponseEntity.ok(assignmentService.getFilterAssignment(search,author,fromStart,toStart,fromCreated,toCreated,isDraft,direction,sortBy,classid.orElse(0),page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/getsubmitassignment")
    public ResponseEntity<?> getNumSubmitAssignment(@RequestParam(value = "assignmentid", required = false) int assignmentid,
                                                    @RequestParam(value = "classid", required = false) int classid) {

        try{
            return ResponseEntity.ok(assignmentService.getNumSubmitAssignment(assignmentid,classid));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
