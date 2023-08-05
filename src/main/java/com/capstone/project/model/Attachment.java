package com.capstone.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String file_name;

    private String file_type; //extension name

    @Column(columnDefinition="TEXT")
    private String file_url;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AttachmentType attachmentType;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
