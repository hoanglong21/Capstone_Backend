package com.capstone.project.controller;
import com.capstone.project.dto.AnswerRequest;
import com.capstone.project.dto.QuestionRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Answer;
import com.capstone.project.model.Question;
import com.capstone.project.service.AnswerService;
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
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class AnswerController {


    @Autowired
    private ModelMapper modelMapper;
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/answers")
    public ResponseEntity<?> getAllAnswer() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/answersbyquestionid")
    public ResponseEntity<?> getAllByQuestionId(@RequestParam int id) {
        return ResponseEntity.ok(answerService.getAllByQuestionId(id));
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<?> getAnswerById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(answerService.getAnswerById(id));
        }catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/createanswers")
        public ResponseEntity<?> createAnswers(@RequestBody List<Answer> answers) {
            try {
                return ResponseEntity.ok(answerService.createAnswers(answers));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }

    @PostMapping("/answers")
    public ResponseEntity<?> createAnswer(@RequestBody Answer answer) {
            try{
                return ResponseEntity.ok(answerService.createAnswer(answer));
            } catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }


    @PutMapping("/answers/{id}")
    public ResponseEntity<?> updateAnswer(@PathVariable int id, @RequestBody Answer answer) {
            try {
                return ResponseEntity.ok(answerService.updateAnswer( id,answer));
            }catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }


    @DeleteMapping("/answers/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable int id) {
        try {
            return ResponseEntity.ok(answerService.deleteAnswer(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
