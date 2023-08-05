package com.capstone.project.dto;

import com.capstone.project.model.Question;
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
public class AnswerRequest {

    private int id;

    private Question question;

    @NotBlank(message = "Answer cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9\\s.,:]+", message = "Answer  can only contain letters, numbers, and spaces")
    @Length(min = 1, message = "Answer  must have at least 1 characters")
    private String content;

    private boolean is_true;

    private String picture;

    private String audio;

    private String video;

    // Custom getter for content
    public String getContent() {
        if (content != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(content.trim());
        }
        return content;
    }
}
