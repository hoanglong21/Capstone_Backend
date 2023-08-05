package com.capstone.project.model;

import com.capstone.project.dto.AssignmentRequest;
import com.capstone.project.dto.ClassRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "AssignmentCustomListMapping",
        classes = @ConstructorResult(
                targetClass = AssignmentRequest.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "due_date", type = Date.class),
                        @ColumnResult(name = "created_date", type = Date.class),
                        @ColumnResult(name = "instruction", type = String.class),
                        @ColumnResult(name = "modified_date", type =Date.class),
                        @ColumnResult(name = "start_date", type = Date.class),
                        @ColumnResult(name = "is_draft", type = Boolean.class),
                        @ColumnResult(name = "userid", type = Integer.class),
                        @ColumnResult(name = "numbersubmit", type = Integer.class),
                        @ColumnResult(name = "author", type = String.class)
                }
        )
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classroom;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    private String title;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date due_date;

    @Column(columnDefinition="TEXT")
    private String instruction;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date start_date;

    private boolean is_draft;
}
