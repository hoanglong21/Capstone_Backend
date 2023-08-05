package com.capstone.project.repository;

import com.capstone.project.model.Content;
import com.capstone.project.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission,Integer> {
    List<Submission> getSubmissionByAssignmentId(int id);

    Submission getByUserIdAndAssignmentId(int userid, int assignmentId);
}
