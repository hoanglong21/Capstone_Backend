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
@Table(name = "achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AchievementType achievementType;

    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    public Achievement(int typeId, String name, String description) {
        AchievementType achievementType = new AchievementType();
        achievementType.setId(typeId);
        this.achievementType = achievementType;
        this.name = name;
        this.description = description;
    }
}
