package com.capstone.project.dto;

import com.capstone.project.model.Class;
import com.capstone.project.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private int id;
    private User user;
    private Class classroom;

   @NotBlank(message = "Post cannot be empty")
//    @Pattern(regexp = "[a-zA-Z0-9\\s.,@!?&:;\\[\\]()\\-]+", message = "Post can only contain letters, numbers, and spaces")
   @Length(min = 1, message = "Post must have at least 1 characters")
    private String content;

    public String getContent() {
        if (content != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(content.trim());
        }
        return content;
    }
}
