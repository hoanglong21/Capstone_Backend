package com.capstone.project.controller;

import com.capstone.project.dto.AttachmentRequest;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Attachment;
import com.capstone.project.service.AttachmentService;
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
public class AttachmentController {

    @Autowired
    private ModelMapper modelMapper;

    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @GetMapping("/attachments")
    public ResponseEntity<?> getAllAttachments() {
        return ResponseEntity.ok( attachmentService.getAllAttachment());
    }
    @GetMapping("/attachmentsbysubmissionid/{id}")
    public ResponseEntity<?> getAttachmentsBySubmissionId(@PathVariable int id) {
       return ResponseEntity.ok( attachmentService.getAllAttachmentBySubmissionId(id));
    }

    @GetMapping("/attachmentsbyassignmentid/{id}")
    public ResponseEntity<?> getAttachmentsByAssignmentId(@PathVariable int id) {
        return ResponseEntity.ok( attachmentService.getAllAttachmentByAssignmentId(id));
    }

    @GetMapping("/attachmentsbypostid/{id}")
    public ResponseEntity<?> getAttachmentsByPostId(@PathVariable int id) {
        return ResponseEntity.ok( attachmentService.getAllAttachmentByPostId(id));
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<?> getAttachmentById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(attachmentService.getAttachmentById(id));
        } catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/createattachments")
    public ResponseEntity<?> createAttachments(@Valid @RequestBody List<AttachmentRequest> attachmentRequests, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            List<Attachment> attachments = attachmentRequests.stream()
                    .map(attachmentRequest -> modelMapper.map(attachmentRequest, Attachment.class))
                    .collect(Collectors.toList());
            try {
                return ResponseEntity.ok(attachmentService.createAttachments(attachments));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }   

    @PostMapping("/attachments")
    public ResponseEntity<?> createAttachment(@Valid @RequestBody AttachmentRequest attachmentRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Attachment attachment = modelMapper.map(attachmentRequest,Attachment.class);
            try{
                return ResponseEntity.ok(attachmentService.createAttachment(attachment));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PutMapping("/attachments/{id}")
    public ResponseEntity<?> updateAttachment(@PathVariable int id,@Valid @RequestBody AttachmentRequest attachmentRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            Attachment attachment = modelMapper.map(attachmentRequest,Attachment.class);
            try {
                return ResponseEntity.ok(attachmentService.updateAttachment(id, attachment));
            } catch (ResourceNotFroundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<?> deleteAttachment(@PathVariable int id) {
        try {
            return ResponseEntity.ok(attachmentService.deleteAttachment(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
