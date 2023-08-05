package com.capstone.project.repository;

import com.capstone.project.model.Assignment;
import com.capstone.project.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Integer> {

    List<Assignment> getAssignmentByClassroomId(int id);
}
