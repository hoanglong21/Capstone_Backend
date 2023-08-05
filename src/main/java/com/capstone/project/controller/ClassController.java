package com.capstone.project.controller;

import com.capstone.project.dto.ClassRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.service.ClassService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
public class ClassController {

    @Autowired
    private ModelMapper modelMapper;
    private final ClassService classService;

@Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/class")
    public ResponseEntity<?> getAllClass() {
        return ResponseEntity.ok(classService.getAllClass());
    }


//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @PostMapping("/class")
    public ResponseEntity<?> createClassroom(@Valid @RequestBody ClassRequest classRequest, BindingResult result) throws ParseException {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Class classroom = modelMapper.map(classRequest,Class.class);
            try{
                return ResponseEntity.ok(classService.createClassroom(classroom));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }


//    @PreAuthorize("hasRole('ROLE_TUTOR') or hasRole('ROLE_LEARNER')" )
    @GetMapping("/class/{id}")
    public ResponseEntity<?> getClassroomById(@PathVariable int id) {
    try {
        return ResponseEntity.ok(classService.getClassroomById(id));
    } catch(ResourceNotFroundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }


//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @PutMapping("/class/{id}")
    public ResponseEntity<?> updateClassroom(@Valid @RequestBody  ClassRequest classRequest, @PathVariable int id,BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Class classroom = modelMapper.map(classRequest,Class.class);
            try {
                return ResponseEntity.ok(classService.updateClassroom(classroom, id));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }


//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @DeleteMapping("/class/{id}")
        public ResponseEntity<?> deleteClass(@PathVariable int id) {
        try {
            return ResponseEntity.ok(classService.deleteClass(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        }

//    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @DeleteMapping("/deleteclass/{id}")
    public ResponseEntity<?> deleteHardClass(@PathVariable int id) {
        try {
            return ResponseEntity.ok(classService.deleteHardClass(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


//    @PreAuthorize("hasRole('ROLE_TUTOR') or hasRole('ROLE_LEARNER')" )
    @GetMapping("/filterclass")
    public ResponseEntity<?> getFilterList(@RequestParam(value = "classid", required = false) Optional<Integer> classid,
                                           @RequestParam(value = "deleted", required = false) Boolean isDeleted,
                                           @RequestParam(value = "search", required = false) String search,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "fromdeleted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromDeleted,
                                           @RequestParam(value = "todeleted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toDeleted,
                                           @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                           @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                           @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

    try{
        return ResponseEntity.ok(classService.getFilterClass(classid.orElse(0),isDeleted,search,author,fromDeleted,toDeleted,fromCreated,toCreated,sortBy,direction,page,size));
    }catch (ResourceNotFroundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @PostMapping("/joinclass")
    public ResponseEntity<?> joinClass(@RequestParam String classCode, @RequestParam String username) {
        try {
            return ResponseEntity.ok(classService.joinClass(classCode,username));
        } catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/resetclasscode/{id}")
    public ResponseEntity<?> ResetClassCode(@PathVariable int id){
        try {
            return ResponseEntity.ok(classService.ResetClassCode(id));
        } catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/checkuserclass")
    public ResponseEntity<?> checkUserClass(@RequestParam int classId, @RequestParam int userId) {
        try {
            return ResponseEntity.ok(classService.CheckUserClass(userId,classId));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/checkuserclasswaiting")
    public ResponseEntity<?> checkUserClassWaiting(@RequestParam int userId,@RequestParam int classId) {
        try {
            return ResponseEntity.ok(classService.CheckUserClassWaiting(userId,classId));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
