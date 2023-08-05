package com.capstone.project.controller;

import com.capstone.project.dto.SubmissionRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.Submission;
import com.capstone.project.service.SubmissionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class SubmissionController {

    @Autowired
    private ModelMapper modelMapper;

    private final SubmissionService submissionService;
@Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/submissions")
    public ResponseEntity<?> getAllSubmission() {
        return ResponseEntity.ok(submissionService.getAllSubmission());
    }

    @GetMapping("/submissionsbyassignmentid/{id}")
    public ResponseEntity<?> getAllSubmissionByAssignmentId(@PathVariable int id) {
        return ResponseEntity.ok(submissionService.getAllSubmissionByAssignmentId(id));
    }

    @PostMapping("/submissions")
    public ResponseEntity<?> createSubmission(@Valid @RequestBody SubmissionRequest submissionRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Submission submission = modelMapper.map(submissionRequest,Submission.class);
            try{
                return ResponseEntity.ok(submissionService.createSubmission(submission));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    @GetMapping("/submissions/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable int id) {
    try {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    } catch (ResourceNotFroundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @GetMapping("/submissionbyauthorandassignment")
    public ResponseEntity<?> getSubmissionByAuthorIdandAssignmentId(@RequestParam(value = "authorid", required = false) int authorid,
                                                                    @RequestParam(value = "assignmentid", required = false) int  assignmentid) {
        try {
            return ResponseEntity.ok(submissionService.getByAuthorIdandAssignmentId(authorid,assignmentid));
        } catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/submissions/{id}")
    public ResponseEntity<?> updateSubmission(@Valid @RequestBody SubmissionRequest submissionRequest, @PathVariable int id,BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Submission submission = modelMapper.map(submissionRequest,Submission.class);
            try {
                return ResponseEntity.ok(submissionService.updateSubmission(id, submission));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable int id) {
        try {
            return ResponseEntity.ok(submissionService.deleteSubmission(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtersubmission")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "authorid", required = false) Optional<Integer> authorId,
                                           @RequestParam(value = "assignmentid", required = false) Optional<Integer> assignmentId,
                                           @RequestParam(value = "mark", required = false) Optional<Double> mark,
                                           @RequestParam(value = "from", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String from,
                                           @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String to,
                                           @RequestParam(value = "direction", required = false) String direction,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        try{
            return ResponseEntity.ok(submissionService.getFilterSubmission(search,authorId.orElse(0),assignmentId.orElse(0),mark.orElse(0.0),from,to,direction,page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
