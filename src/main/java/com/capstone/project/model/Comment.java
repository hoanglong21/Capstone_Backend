package com.capstone.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    @Column(columnDefinition="TEXT")
    private String content;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    @ManyToOne(cascade={CascadeType.MERGE})
    @JoinColumn(name = "root_id")
    private Comment root;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private CommentType commentType;

//    private int destination_id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "studyset_id", referencedColumnName = "id")
    private StudySet studySet;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private Submission submission;
}
