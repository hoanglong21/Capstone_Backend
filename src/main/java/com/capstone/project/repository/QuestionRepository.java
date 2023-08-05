package com.capstone.project.repository;

import com.capstone.project.model.Content;
import com.capstone.project.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    List<Question> getQuestionByTestId(int id);
}
