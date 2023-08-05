package com.capstone.project.controller;

import com.capstone.project.dto.CommentRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Comment;
import com.capstone.project.model.Content;
import com.capstone.project.service.CommentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class CommentController {

    @Autowired
    private ModelMapper modelMapper;

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/comments")
    public ResponseEntity<?> getAllComment(){
        return ResponseEntity.ok(commentService.getAllComment());
    }
    @GetMapping("/commentbypostid/{id}")
    public ResponseEntity<?> getAllCommentByPostId(@PathVariable int id){
            return ResponseEntity.ok(commentService.getAllCommentByPostId(id));
    }

    @GetMapping("/commentbyrootid/{id}")
    public ResponseEntity<?> getAllCommentByRootId(@PathVariable int id){
        return ResponseEntity.ok(commentService.getAllCommentByRootId(id));
    }

    @GetMapping("/commentbystudysetid/{id}")
    public ResponseEntity<?> getAllCommentByStudysetId(@PathVariable int id){
        return ResponseEntity.ok(commentService.getAllCommentByStudySetId(id));
    }

    @GetMapping("/commentbytestid/{id}")
    public ResponseEntity<?> getAllCommentByTestId(@PathVariable int id){
        return ResponseEntity.ok(commentService.getAllCommentByTestId(id));
    }

    @GetMapping("/commentbasignmentid/{id}")
    public ResponseEntity<?> getAllCommentByAssignmentId(@PathVariable int id){
        return ResponseEntity.ok(commentService.getAllCommentByAssignmentId(id));
    }

    @GetMapping("/commentbysubmisionid/{id}")
    public ResponseEntity<?> getAllCommentBySubmisionId(@PathVariable int id){
        return ResponseEntity.ok(commentService.getAllCommentBySubmisionId(id));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable int id){
        try {
            return ResponseEntity.ok(commentService.getCommentById(id));
        } catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentRequest commentRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
                 Comment comment = modelMapper.map(commentRequest,Comment.class);
                 try{
                     return ResponseEntity.ok(commentService.createComment(comment));
                 } catch (Exception e) {
                     return ResponseEntity.badRequest().body(e.getMessage());
                 }
        }

    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@PathVariable int id,@Valid @RequestBody CommentRequest commentRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Comment comment = modelMapper.map(commentRequest,Comment.class);
            try {
                return ResponseEntity.ok(commentService.updateComment(comment, id));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id) {
        try {
            return ResponseEntity.ok(commentService.deleteComment(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtercomment")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "direction", required = false) String direction,
                                           @RequestParam(value = "typeid", required = false) Optional<Integer> typeid,
                                           @RequestParam(value = "postid", required = false) Optional<Integer> postid,
                                           @RequestParam(value = "testid", required = false) Optional<Integer> testid,
                                           @RequestParam(value = "studysetid", required = false) Optional<Integer> studysetid,
                                           @RequestParam(value = "assignmentid", required = false) Optional<Integer> assignmentid,
                                           @RequestParam(value = "submissionid", required = false) Optional<Integer> submissionid,
                                           @RequestParam(value = "rootid", required = false) Optional<Integer> roottid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        try{
            return ResponseEntity.ok(commentService.getFilterComment(search,author,direction,typeid.orElse(0),postid.orElse(0),testid.orElse(0),studysetid.orElse(0),assignmentid.orElse(0),submissionid.orElse(0),roottid.orElse(0),page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
