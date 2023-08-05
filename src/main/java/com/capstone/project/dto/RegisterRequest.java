package com.capstone.project.dto;

import com.capstone.project.util.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Invalid username. Only letters, numbers, and underscores are allowed.")
    @Length(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    private String username;

    @Pattern(regexp = "^[a-zA-ZÀ-ỹ ]+$", message = "First name must contain letters only")
    @Length(min = 1, max = 30, message = "First name must be between 1 and 30 characters")
    private String first_name;

    @Pattern(regexp = "^[a-zA-ZÀ-ỹ ]+$", message = "Last name must contain letters only")
    @Length(min = 1, max = 30, message = "Last name must be between 1 and 30 characters")
    private String last_name;

    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email. Only letters, numbers, and dot are allowed.")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit (number)")
    @Length(min = 8, message = "Password at least 8 characters")
    private String password;

    @Pattern(regexp = "ROLE_ADMIN|ROLE_LEARNER|ROLE_TUTOR", message = "Role input is not correct")
    private String role;

    public void setFirst_name(String first_name) {
        if(first_name!=null){
            String formattedFirstName = first_name.trim().replaceAll("\\s+", " ");
            this.first_name = Arrays.stream(formattedFirstName.split("\\s"))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));
        }
    }

    public void setLast_name(String last_name) {
        if(last_name!=null){
            String formattedFirstName = last_name.trim().replaceAll("\\s+", " ");
            this.last_name = Arrays.stream(formattedFirstName.split("\\s"))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));
        }
    }
}
