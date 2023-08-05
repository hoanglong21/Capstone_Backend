package com.capstone.project.repository;

import com.capstone.project.model.Card;
import com.capstone.project.model.StudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> getCardByStudySetId(int id);

    @Query(value = "WITH filterProgress AS (SELECT * FROM progress WHERE user_id = :userId) " +
            "SELECT c.* FROM filterProgress p " +
            "RIGHT JOIN card c ON p.card_id = c.id " +
            "WHERE COALESCE(p.user_id, :userId) = :userId AND COALESCE(p.status, 'not studied') IN :statuses AND c.studyset_id = :studysetId", nativeQuery = true)
    List<Card> findCardByProgress(@Param("userId") int userId, @Param("statuses") String[] statuses, @Param("studysetId") int studysetId);

    @Query(value = "WITH filterProgress AS (SELECT * FROM progress WHERE user_id = :userId) " +
            "SELECT c.* FROM filterProgress p RIGHT JOIN card c ON p.card_id = c.id " +
            " WHERE COALESCE(p.user_id, :userId) = :userId AND COALESCE(p.status, 'not studied') IN :status " +
            " AND c.studyset_id = :studySetId AND (:star = false OR COALESCE(p.is_star, false) = :star) " +
            " ORDER BY CASE COALESCE(p.status, 'not studied') " +
            "          WHEN 'not studied' THEN 1 " +
            "          WHEN 'still learning' THEN 2 " +
            "          WHEN 'mastered' THEN 3 " +
            "          ELSE 4 " +
            "          END, " +
            " CASE WHEN :direction = 'asc' THEN :sortBy END ASC, " +
            " CASE WHEN :direction = 'desc' THEN :sortBy END DESC", nativeQuery = true)
    List<Card> getCardInSetWithCondition(int studySetId, int userId, String[] status, boolean star, String sortBy, String direction);
}
