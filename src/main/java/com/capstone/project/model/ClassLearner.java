package com.capstone.project.model;

import com.capstone.project.dto.ClassLearnerRequest;
import com.capstone.project.dto.ClassRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@SqlResultSetMapping(
        name = "ClassLearnerListMapping",
        classes = @ConstructorResult(
                targetClass = ClassLearnerRequest.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "created_date", type = Date.class),
                        @ColumnResult(name = "accepted", type =Boolean.class),
                        @ColumnResult(name = "username", type =String.class),
                        @ColumnResult(name = "avatar", type =String.class),

                }
        )
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "class_learner", uniqueConstraints = @UniqueConstraint(columnNames={"user_id", "class_id"}))
public class ClassLearner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean is_accepted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classroom;

    @Column
    @Temporal(TemporalType.DATE)
    private Date created_date;
}
