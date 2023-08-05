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
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private HistoryType historyType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Class classroom;

    @ManyToOne
    @JoinColumn(name = "studyset_id", referencedColumnName = "id")
    private StudySet studySet;
}
