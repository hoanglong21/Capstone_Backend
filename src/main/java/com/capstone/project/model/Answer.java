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
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition="TEXT")
    private String content;

    private boolean is_true;

    @Column(columnDefinition="TEXT")
    private String picture;

    @Column(columnDefinition="TEXT")
    private String audio;

    @Column(columnDefinition="TEXT")
    private String video;
}
