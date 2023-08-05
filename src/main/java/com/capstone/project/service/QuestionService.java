package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Answer;
import com.capstone.project.model.Question;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    List<Question> getAllQuestions();

    List<Question> getAllByTestId(int id);

    Question createQuestion( Question question);

    List<Question> createQuestions(List<Question> questions);

    Question getQuestionById( int id) throws ResourceNotFroundException;

    Question updateQuestion ( int id, Question question) throws ResourceNotFroundException;

    Boolean deleteQuestion ( int id) throws ResourceNotFroundException;

    Map<String, Object> getFilterQuestion(String search, int typeid,int testid, int page, int size) throws ResourceNotFroundException;
}
