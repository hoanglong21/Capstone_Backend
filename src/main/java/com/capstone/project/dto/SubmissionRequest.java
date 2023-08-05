package com.capstone.project.dto;

import com.capstone.project.model.Assignment;
import com.capstone.project.model.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionRequest {

    private int id;

    private User user;

    private Assignment assignment;

    private double mark;

    private boolean is_done;


    @Pattern(regexp = ".*|[a-zA-Z0-9\\s.,+&()-]+", message = "Description can only contain letters, numbers, and spaces")
    private String description;

    public String getDescription() {
        if (description != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(description.trim());
        }
        return description;
    }
}
