package com.capstone.project.dto;

import com.capstone.project.model.Class;
import com.capstone.project.model.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {

    private int id;
    private Class classroom;
    private User user;

    @NotNull(message = "Title cannot be null")
//    @Pattern(regexp = "[a-zA-Z0-9\\s?+?/]+", message = "Title can only contain letters, numbers, and spaces")
    @Length(min = 2, message = "Title must have at least 2 characters")
    private String title;

    private Date created_date;

    private Date modified_date;


//    @FutureOrPresent
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start_date;

    private boolean is_draft;

    int submitted;
    int notsubmitted;


//    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private Date due_date;

//    @Pattern(regexp = "[a-zA-Z0-9\\s]+", message = " Can only contain letters, numbers, and spaces")
    private String instruction;

    int numbersubmit;

    int userid;

    String author;

    public String getTitle() {
        if (title != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(title.trim());
        }
        return title;
    }

    public String getInstruction() {
        if (instruction != null) {
            // Remove extra spaces between words
            return StringUtils.normalizeSpace(instruction.trim());
        }
        return instruction;
    }

    @AssertTrue(message = "Due date must be greater than or equal to start date")
    public boolean isDueDateValid() {
        return due_date == null || start_date == null || due_date.compareTo(start_date) >= 0;
    }

//    @AssertTrue(message = "Start date must be equal or after created date")
//    public boolean isStartDateValid() {
//        return start_date == null || start_date.compareTo(new Date()) >= 0;
//    }

}
