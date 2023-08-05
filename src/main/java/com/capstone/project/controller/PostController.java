package com.capstone.project.controller;

import com.capstone.project.dto.PostRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.Post;
import com.capstone.project.service.PostService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
public class PostController {

    @Autowired
    private ModelMapper modelMapper;

    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/post")
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    @GetMapping("/postbyclassid/{id}")
    public ResponseEntity<?> getAllPostByClassId(@PathVariable int id) {
        return ResponseEntity.ok(postService.getAllPostByClassId(id));
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, BindingResult result)  {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Post post = modelMapper.map(postRequest,Post.class);
            try{
                return ResponseEntity.ok(postService.createPost(post));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
    try {
        return ResponseEntity.ok(postService.getPostById(id));
    } catch (ResourceNotFroundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostRequest postRequest, @PathVariable int id,
                                        BindingResult result ) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Post post = modelMapper.map(postRequest,Post.class);
            try {
                return ResponseEntity.ok(postService.updatePost(post, id));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        try {
            return ResponseEntity.ok(postService.deletePost(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filterpost")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                           @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                           @RequestParam(value = "classid", required = false) Optional<Integer> classid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        try{
            return ResponseEntity.ok(postService.getFilterPost(search,author,fromCreated,toCreated,sortBy,direction,classid.orElse(0),page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
