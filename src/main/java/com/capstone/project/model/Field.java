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
@Table(name = "field")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private StudySetType studySetType;

    private String name;

    public Field(int id, String name) {
        StudySetType studySetType = new StudySetType();
        studySetType.setId(id);
        this.studySetType = studySetType;
        this.name = name;
    }
}
