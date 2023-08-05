package com.capstone.project.dto;

import com.capstone.project.util.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
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
public class UserRequest {
    private int id;

    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^\\S+$", message = "Username not allow space")
    private String username;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must contain letters only")
    private String first_name;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must contain letters only")
    private String last_name;

    @Pattern(regexp = "male|female", message = "Gender must be male or female")
    private String gender;

    @Past(message = "Date of birth must be in the past")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dob;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    @Pattern(regexp = "^\\S+$", message = "Phone not allow space")
    private String phone;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit (number)")
    @Length(min = 8, message = "Password at least 8 characters")
    private String password;

    @Pattern(regexp = "ROLE_ADMIN|ROLE_LEARNER|ROLE_TUTOR", message = "Role input is not correct")
    private String role;

    private String address;

    private String bio;

    @Pattern(regexp = "deleted|banned|active|pending", message = "Status input is not correct")
    private String status;

    private String avatar;

    public void setFirst_name(String first_name) {
        String formattedFirstName = first_name.trim().replaceAll("\\s+", " ");
        this.first_name = Arrays.stream(formattedFirstName.split("\\s"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public void setLast_name(String last_name) {
        String formattedFirstName = last_name.trim().replaceAll("\\s+", " ");
        this.last_name = Arrays.stream(formattedFirstName.split("\\s"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
