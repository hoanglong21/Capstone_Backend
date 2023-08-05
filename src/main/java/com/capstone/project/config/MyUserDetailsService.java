package com.capstone.project.config;

import com.capstone.project.model.User;
import com.capstone.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            user = repository.findUserByEmail(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new MyUserDetails(user);
    }
}