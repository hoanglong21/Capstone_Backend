package com.capstone.project.model;

import com.capstone.project.dto.StudySetResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


@SqlResultSetMapping(
        name = "StudySetResponseCustomListMapping",
        classes = @ConstructorResult(
                targetClass = StudySetResponse.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "is_deleted", type = Boolean.class),
                        @ColumnResult(name = "is_public", type = Boolean.class),
                        @ColumnResult(name = "is_draft", type = Boolean.class),
                        @ColumnResult(name = "type_id", type = Integer.class),
                        @ColumnResult(name = "author_id", type = Integer.class),
                        @ColumnResult(name = "deleted_date", type = Date.class),
                        @ColumnResult(name = "count", type = Integer.class),
                        @ColumnResult(name = "author", type = String.class),
                        @ColumnResult(name = "created_date", type = Date.class),
                        @ColumnResult(name = "author_firstname", type = String.class),
                        @ColumnResult(name = "author_lastname", type = String.class),
                        @ColumnResult(name = "avatar", type = String.class)
                }
        )
)

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "studyset")
public class StudySet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    private boolean is_deleted; // delete 1 or not 0

    private boolean is_public; // public 1 or private 0

    private boolean is_draft; // draft 1 or not 0

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private StudySetType studySetType;

    @ManyToMany(mappedBy = "studySets")
    Set<Class> classes;

    @Column
    @Temporal(TemporalType.DATE)
    private Date deleted_date;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;
}
