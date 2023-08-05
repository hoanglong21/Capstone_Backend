package com.capstone.project.controller;

import com.capstone.project.dto.TestRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Test;
import com.capstone.project.model.TestResult;
import com.capstone.project.service.TestService;
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
public class TestController {

    @Autowired
    private ModelMapper modelMapper;

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> getAllTest(){
        return ResponseEntity.ok(testService.getAllTest());
    }

    @GetMapping("/testbyusername/{name}")
    public ResponseEntity<?> getTestByUsername(@PathVariable String username){
        try {
            return ResponseEntity.ok(testService.getTestByUser(username));
        } catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test/{id}")
        public ResponseEntity<?> getTestById(@PathVariable int id){
        try {
            return ResponseEntity.ok(testService.getTestById(id));
        } catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/testbyclassid/{id}")
    public ResponseEntity<?> getAllTestByClassId(@PathVariable int id) {
        return ResponseEntity.ok(testService.getAllTestByClassId(id));
    }

    @PostMapping("/test")
    public ResponseEntity<?> createTest(@RequestBody Test test) {
            try{
                return ResponseEntity.ok(testService.createTest(test));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }


    }
    @PutMapping ("/test/{id}")
        public ResponseEntity<?> updateTest(@PathVariable int id, @RequestBody Test test){
            try {
                return ResponseEntity.ok(testService.updateTest(id, test));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }


    }

    @DeleteMapping("/test/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable int id) {
        try {
            return ResponseEntity.ok(testService.deleteTest(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtertest")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "fromstarted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromStart,
                                           @RequestParam(value = "tostarted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toStart,
                                           @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                           @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                           @RequestParam(value = "draft", required = false) Boolean isDraft,
                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                           @RequestParam(value = "duration", required = false) Optional<Integer> duration,
                                           @RequestParam(value = "classid", required = false) Optional<Integer> classid,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        try{
            return ResponseEntity.ok(testService.getFilterTest(search,author,direction,duration.orElse(0),classid.orElse(0),fromStart,toStart,fromCreated,toCreated,isDraft, sortBy,page,size));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/starttest")
    public ResponseEntity<?> startTest(@RequestParam("userid") int userId,
                                       @RequestParam("testid") int testId){
        return ResponseEntity.ok(testService.startTest(testId, userId));
    }

    @PostMapping("/endtest")
    public ResponseEntity<?> endTest(@RequestBody List<TestResult> testResultList) {
        try{
            return ResponseEntity.ok(testService.endTest(testResultList));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
