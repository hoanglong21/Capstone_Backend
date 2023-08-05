package com.capstone.project.repository;

import com.capstone.project.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    @Query(value = "SELECT p.* FROM Progress p WHERE p.user_id = :userId AND p.card_id = :cardId", nativeQuery = true)
    Progress findByCardIdAndUserId(int userId, int cardId);

    @Query(value = "WITH filterProgress AS (SELECT * FROM progress WHERE user_id = :userId) " +
            "SELECT p.* FROM filterProgress p INNER JOIN card c ON p.card_id = c.id WHERE p.user_id = :userId AND c.studyset_id = :studySetId", nativeQuery = true)
    List<Progress> findByStudySetIdAndUserId(int userId, int studySetId);
}
