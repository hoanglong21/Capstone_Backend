package com.capstone.project.repository;

import com.capstone.project.model.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackTypeRepository extends JpaRepository<FeedbackType, Integer> {
}
