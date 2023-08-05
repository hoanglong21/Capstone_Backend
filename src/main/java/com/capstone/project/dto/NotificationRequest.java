package com.capstone.project.dto;

import com.capstone.project.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private int id;

    private User user;

    @NotBlank(message = "Title cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9\\s]+", message = "Title can only contain letters, numbers, and spaces")
    @Length(min = 2, message = "Title must have at least 2 characters")
    private String title;

    @Pattern(regexp = "^$|[a-zA-Z0-9\\s.,:+-]+", message = " Can only contain letters, numbers, and spaces")
    private String content;

    private String url;
}
