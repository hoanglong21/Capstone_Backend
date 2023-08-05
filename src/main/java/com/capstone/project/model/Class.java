package com.capstone.project.model;

import com.capstone.project.dto.ClassRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@SqlResultSetMapping(
        name = "ClassCustomListMapping",
        classes = @ConstructorResult(
                targetClass = ClassRequest.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "class_name", type = String.class),
                        @ColumnResult(name = "classcode", type = String.class),
                        @ColumnResult(name = "created_date", type = Date.class),
                        @ColumnResult(name = "deleted_date", type = Date.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "is_deleted", type =Boolean.class),
                        @ColumnResult(name = "author_id", type = Integer.class),
                        @ColumnResult(name = "member", type = Integer.class),
                        @ColumnResult(name = "studyset", type = Integer.class),
                        @ColumnResult(name = "avatar", type = String.class),
                        @ColumnResult(name = "author", type = String.class)
                }
        )
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "class")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    @Column
    @Temporal(TemporalType.DATE)
    private Date created_date;

    private String class_name;

    @Column(unique = true)
    private String classcode;

    @Column(columnDefinition="TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "class_studyset",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "sudyset_id"))
    Set<StudySet> studySets;

    private boolean is_deleted;

    @Column
    @Temporal(TemporalType.DATE)
    private Date deleted_date;
}
