package com.capstone.project.controller;

import com.capstone.project.dto.ChangePasswordRequest;
import com.capstone.project.dto.CheckPasswordRequest;
import com.capstone.project.dto.UserRequest;
import com.capstone.project.dto.UserUpdateRequest;
import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;
import com.capstone.project.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users/{username}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @Valid @RequestBody UserUpdateRequest userUpdateRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            User userDetails = modelMapper.map(userUpdateRequest, User.class);
            try {
                return ResponseEntity.ok(userService.updateUser(username, userDetails));
            } catch (ResourceNotFroundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (DuplicateValueException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @GetMapping("/otherusers")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> findAllNameExcept(@RequestParam("username") String username,
                                               @RequestParam("except") String except) {
        return ResponseEntity.ok(userService.findAllNameExcept(username, except));
    }

    @GetMapping("/users/{username}/ban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> banUser(@PathVariable("username") String username) {
        try {
            if(userService.banUser(username)) {
                return ResponseEntity.ok("Banned successfully");
            } else {
                return ResponseEntity.badRequest().body("Ban fail");
            }
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{username}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        try {
            return ResponseEntity.ok(userService.deleteUser(username));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{username}/recover")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> recoverUser(@PathVariable("username") String username) {
        try {
            if(userService.recoverUser(username)) {
                return ResponseEntity.ok("Recover successfully");
            } else {
                return ResponseEntity.badRequest().body("Wait at least 7 days to recover");
            }
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sendverify")
    @PreAuthorize("hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> sendVerificationEmail(@RequestParam("username") String username) {
        try {
            return ResponseEntity.ok(userService.sendVerificationEmail(username));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify")
    @PreAuthorize("hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        try {
            return ResponseEntity.ok(userService.verifyAccount(token));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sendreset")
//    @PreAuthorize("hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> sendResetPasswordEmail(@RequestParam("username") String username) {
        try {
            return ResponseEntity.ok(userService.sendResetPasswordEmail(username));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset")
//    @PreAuthorize("hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> resetPassword(@RequestParam("username") String username, @RequestParam("pin") String pin,
                                           @Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                if(userService.resetPassword(username, pin, changePasswordRequest.getPassword())) {
                    return ResponseEntity.ok("Change password successfully");
                } else {
                    return ResponseEntity.badRequest().body("Change password fail, check the newest mail");
                }

            } catch (ResourceNotFroundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PostMapping("/checkpassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> checkMatchPassword(@RequestParam("username") String username,
                                                @Valid @RequestBody CheckPasswordRequest checkPasswordRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                return ResponseEntity.ok(userService.checkMatchPassword(username, checkPasswordRequest.getPassword()));
            } catch (ResourceNotFroundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    }

    @PostMapping("/changepassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> changePassword(@RequestParam("username") String username,
                                            @Valid @RequestBody ChangePasswordRequest changePasswordRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                if(userService.changePassword(username, changePasswordRequest.getPassword())) {
                    return ResponseEntity.ok("Change password successfully");
                } else {
                    return ResponseEntity.badRequest().body("Change password fail");
                }
            } catch (ResourceNotFroundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @GetMapping("/filterusers")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEARNER') || hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> filterUser(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                                        @RequestParam(value = "username", required = false, defaultValue = "") String username,
                                        @RequestParam(value = "email", required = false, defaultValue = "") String email,
                                        @RequestParam(value = "gender", required = false, defaultValue = "") String gender,
                                        @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
                                        @RequestParam(value = "role", required = false, defaultValue = "") String[] role,
                                        @RequestParam(value = "address", required = false, defaultValue = "") String address,
                                        @RequestParam(value = "bio", required = false, defaultValue = "") String bio,
                                        @RequestParam(value = "status", required = false, defaultValue = "") String[] status,
                                        @RequestParam(value = "fromdob", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromDob,
                                        @RequestParam(value = "todob", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toDob,
                                        @RequestParam(value = "frombanned", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromBanned,
                                        @RequestParam(value = "tobanned", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toBanned,
                                        @RequestParam(value = "fromdeleted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromDeleted,
                                        @RequestParam(value = "todeleted", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toDeleted,
                                        @RequestParam(value = "fromcreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String fromCreated,
                                        @RequestParam(value = "tocreated", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") String toCreated,
                                        @RequestParam(value = "sortby", required = false, defaultValue = "created_date") String sortBy,
                                        @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(userService.filterUser(name, username, email, gender, phone, role, address, bio, status,
                    fromDob, toDob, fromBanned, toBanned, fromDeleted, toDeleted, fromCreated, toCreated, sortBy, direction, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Check the input again");
        }
    }
}
