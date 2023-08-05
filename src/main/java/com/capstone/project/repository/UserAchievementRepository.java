package com.capstone.project.repository;

import com.capstone.project.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {

    List<UserAchievement> getUserAchievementByUserId(int id);
}
