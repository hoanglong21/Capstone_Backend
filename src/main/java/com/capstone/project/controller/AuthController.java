package com.capstone.project.controller;

import com.capstone.project.dto.AuthenticationRequest;
import com.capstone.project.dto.RegisterRequest;
import com.capstone.project.dto.UserRequest;
import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.model.User;
import com.capstone.project.service.JwtService;
import com.capstone.project.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            // create a list of error messages from the binding result
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } else {

            User user = modelMapper.map(registerRequest, User.class);

            try {
                User createdUser = userService.createUser(user);
                return ResponseEntity.ok(createdUser);
            } catch (DuplicateValueException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> AuthenticateAndGetToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(authRequest.getUsername());
                return ResponseEntity.ok(jwtToken);
            } else {
                return ResponseEntity.badRequest().body("The login details you provided are incorrect. Please try again.");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("The login details you provided are incorrect. Please try again.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("token") String token) {
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Logout successful");
    }

}
