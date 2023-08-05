package com.capstone.project.dto;

import com.capstone.project.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRequest {

    private int id;
    private User user;
    private Date created_date;

    @NotBlank(message = "Classname cannot be empty")
//    @Pattern(regexp = "[^\\s].*[^\\s]", message = "Class name must not start or end with whitespace")
    @Pattern(regexp = "[a-zA-Z0-9\\s]+", message = "Class name can only contain letters, numbers, and spaces")
    @Length(min = 3, message = "Class name must have at least 3 characters")
    private String class_name;

    private String classcode;

//    @Pattern(regexp = "[^\\s].*[^\\s]", message = "Description must not start or end with whitespace")
    @Pattern(regexp = "^$|[a-zA-Z0-9\\s.,:;!?+&\\[\\]()\\-]+", message = "Description can only contain letters, numbers, and spaces")
    private String description;

    private Date deleted_date;

    private boolean is_deleted;

    private int member;

    private int studyset;

    private int author_id;

    private String author;
    private String avatar;

    // Custom getter for class_name
    public String getClass_name() {
        if (class_name != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(class_name.trim());
        }
        return class_name;
    }


    // Custom getter for description
    public String getDescription() {
        if (description != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(description.trim());
        }
        return description;
    }

}
