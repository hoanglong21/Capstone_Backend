package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Answer;
import java.util.List;

public interface AnswerService {

    List<Answer> getAllAnswers();

    List<Answer> getAllByQuestionId(int id);

    Answer createAnswer( Answer answer);

    List<Answer> createAnswers(List<Answer> answerlist);

    Answer getAnswerById( int id) throws ResourceNotFroundException;

    Answer updateAnswer ( int id, Answer answer) throws ResourceNotFroundException;

    Boolean deleteAnswer ( int id) throws ResourceNotFroundException;
}
