package com.capstone.project.model;

import com.capstone.project.dto.StudySetResponse;
import com.capstone.project.dto.TestRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.List;

@SqlResultSetMapping(
        name = "TestCustomListMapping",
        classes = @ConstructorResult(
                targetClass = TestRequest.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "created_date", type = Date.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "duration", type = Float.class),
                        @ColumnResult(name = "modified_date", type = Date.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "author_id", type = Integer.class),
                        @ColumnResult(name = "class_id", type = Integer.class),
                        @ColumnResult(name = "due_date", type = Date.class),
                        @ColumnResult(name = "is_draft", type = Boolean.class),
                        @ColumnResult(name = "num_attemps", type = Integer.class),
                        @ColumnResult(name = "start_date", type = Date.class),
                        @ColumnResult(name = "authorname", type = String.class),
                        @ColumnResult(name = "classname", type = String.class),
                        @ColumnResult(name = "totalquestion", type = Integer.class),
                        @ColumnResult(name = "totalcomment", type = Integer.class)
                }
        )
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classroom;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified_date;

    @Column(columnDefinition="TEXT")
    private String description;

    private float duration;

    private boolean is_draft;

    private int num_attemps;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date start_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date due_date;

}
