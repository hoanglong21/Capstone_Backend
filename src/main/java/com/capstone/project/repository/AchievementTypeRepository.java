package com.capstone.project.repository;

import com.capstone.project.model.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementTypeRepository extends JpaRepository<AchievementType, Integer> {
}
