package com.capstone.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPasswordRequest {
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
