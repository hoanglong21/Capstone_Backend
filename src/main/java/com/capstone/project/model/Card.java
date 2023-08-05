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
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition="TEXT")
    private String picture;

    @Column(columnDefinition="TEXT")
    private String audio;

    @ManyToOne
    @JoinColumn(name = "studyset_id", nullable = false)
    private StudySet studySet;
}
