package com.capstone.project.repository;

import com.capstone.project.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
}
