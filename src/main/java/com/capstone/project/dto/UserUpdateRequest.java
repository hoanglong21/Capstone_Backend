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

import javax.annotation.Nullable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ ]+$", message = "First name must contain letters only")
    @Length(min = 1, max = 30, message = "First name must be between 1 and 30 characters")
    private String first_name;

    @Pattern(regexp = "^[a-zA-ZÀ-ỹ ]+$", message = "Last name must contain letters only")
    @Length(min = 1, max = 30, message = "Last name must be between 1 and 30 characters")
    private String last_name;

    @Pattern(regexp = "male|female", message = "Gender must be male or female")
    private String gender;

    @Past(message = "Date of birth must be in the past")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dob;

    @Nullable
    @Pattern(regexp = "^(|\\d{10})$", message = "Phone can be empty or must be exactly 10 digits")
    private String phone;

    private String address;

    private String bio;

    private String avatar;
}
