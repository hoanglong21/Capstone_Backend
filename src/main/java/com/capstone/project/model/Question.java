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
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition="TEXT")
    private String question;

    @Column(columnDefinition="TEXT")
    private String picture;

    @Column(columnDefinition="TEXT")
    private String audio;

    @Column(columnDefinition="TEXT")
    private String video;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private QuestionType questionType;

    private int num_choice;

    private double point;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
