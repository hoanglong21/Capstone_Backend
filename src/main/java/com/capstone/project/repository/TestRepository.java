package com.capstone.project.repository;

import com.capstone.project.model.Question;
import com.capstone.project.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test,Integer> {
    @Query(value = "SELECT * FROM test WHERE author_id = :id", nativeQuery = true)
    List<Test> getTestByAuthorId(int id);

    List<Test> getTestByClassroomId(int id);
}
