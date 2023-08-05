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
@Table(name = "test_learner")
public class TestLearner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int num_attempt;

    private double mark;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
}
