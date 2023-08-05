package com.capstone.project.controller;

import com.capstone.project.dto.ClassRequest;
import com.capstone.project.dto.NotificationRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.Notification;
import com.capstone.project.service.NotificationService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class NotificationController {

    @Autowired
    private ModelMapper modelMapper;

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notification")
    public ResponseEntity<?> getAllNotification() {
        return ResponseEntity.ok(notificationService.getAllNotification());
    }


    @PostMapping("/notification")
    public ResponseEntity<?> createNotification(@Valid @RequestBody NotificationRequest notificationRequest, BindingResult result) throws ParseException {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Notification notification = modelMapper.map(notificationRequest,Notification.class);
            try{
                return ResponseEntity.ok(notificationService.createNotification(notification));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @GetMapping("/notification/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationById(id));
        } catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/notification/{id}")
    public ResponseEntity<?> updateNotification(@Valid @RequestBody  NotificationRequest notificationRequest, @PathVariable int id,BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Notification notification = modelMapper.map(notificationRequest,Notification.class);
            try {
                return ResponseEntity.ok(notificationService.updateNotification(notification,id));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @DeleteMapping("/notification/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable int id) {
        try {
            return ResponseEntity.ok(notificationService.deleteNotification(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
