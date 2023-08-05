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
@Table(name = "progress", uniqueConstraints = @UniqueConstraint(columnNames={"user_id", "card_id"}))
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    private String status;
    /*<Still learning: r>0 or w>0,
    Mastered: r >=2,
    Not studied: r-0 and w-0>*/

    // Use backticks to escape the reserved keyword
    @Column(name = "`right`")
    private int right;

    private int wrong;

    private int total_wrong;

    private boolean is_star;

    @Column(columnDefinition="TEXT")
    private String note;

    @Column(columnDefinition="TEXT")
    private String picture;

    @Column(columnDefinition="TEXT")
    private String audio;

}
