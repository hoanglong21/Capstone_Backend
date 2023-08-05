package com.capstone.project.dto;

import com.capstone.project.model.Assignment;
import com.capstone.project.model.AttachmentType;
import com.capstone.project.model.Post;
import com.capstone.project.model.Submission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentRequest {

    private int id;

//    @NotBlank(message = "Cannot be empty")
//    @Pattern(regexp = "[a-zA-Z0-9\\s.]+", message = "File can only contain letters, numbers, and spaces")
    private String file_name;

//    @NotBlank(message = "Cannot be empty")
//    @Pattern(regexp = "[a-zA-Z0-9\\s.]+", message = "File can only contain letters, numbers, and spaces")
//    @Pattern(regexp = "^(.+)\\.(doc|pdf|png|jpe?g)$", message = "Invalid file format. Only DOC, PDF, PNG, JPG, and JPEG files are allowed.")
    private String file_url;


//    @NotBlank(message = "Cannot be empty")
//    @Pattern(regexp = "[a-zA-Z0-9\\s.]+", message = "File can only contain letters, numbers, and spaces")
    private String file_type;

    private AttachmentType attachmentType;

    private Assignment assignment;

    private Submission submission;

    private Post post;
}
