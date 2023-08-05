package com.capstone.project.controller;

import com.capstone.project.dto.AttachmentRequest;
import com.capstone.project.dto.QuestionRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Attachment;
import com.capstone.project.model.Question;
import com.capstone.project.service.QuestionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
public class QuestionController {

    @Autowired
    private ModelMapper modelMapper;

    private final QuestionService questionService;
@Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public ResponseEntity<?>  getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/questionsbytestid")
    public ResponseEntity<?> getAllByTestId(@RequestParam int id) {
        return ResponseEntity.ok(questionService.getAllByTestId(id));
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable int id) {
    try {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    } catch (ResourceNotFroundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @PostMapping("/createquestions")
    public ResponseEntity<?> createQuestions(@RequestBody List<Question> questions) {
            try {
                return ResponseEntity.ok(questionService.createQuestions(questions));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }

    @PostMapping("/questions")
    public ResponseEntity<?> createQuestion( @RequestBody Question question) {
            try{
                return ResponseEntity.ok(questionService.createQuestion(question));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable int id, @RequestBody Question question) {
            try {
                return ResponseEntity.ok(questionService.updateQuestion(id, question));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }


    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable int id) {
        try {
            return ResponseEntity.ok(questionService.deleteQuestion(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filterquestion")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "typeid", required = false) Optional<Integer> typeid,
                                           @RequestParam(value = "testid", required = false) Optional<Integer> testid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "1") int size) {

        try{
            return ResponseEntity.ok(questionService.getFilterQuestion(search,typeid.orElse(0),testid.orElse(0),page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
