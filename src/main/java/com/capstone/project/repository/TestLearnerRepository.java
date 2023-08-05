package com.capstone.project.repository;

import com.capstone.project.model.Progress;
import com.capstone.project.model.TestLearner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestLearnerRepository extends JpaRepository<TestLearner, Integer> {
    @Query(value = "SELECT t.* FROM test_learner t WHERE t.user_id = :userId AND t.test_id = :testId", nativeQuery = true)
    List<TestLearner> findByTestIdAndUserId(int userId, int testId);
}
