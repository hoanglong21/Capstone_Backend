package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Answer;
import com.capstone.project.model.Card;
import com.capstone.project.repository.AnswerRepository;
import com.capstone.project.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @Override
    public List<Answer> getAllByQuestionId(int id) {
        return answerRepository.getAnswerByQuestionId(id);
    }

    @Override
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public List<Answer> createAnswers(List<Answer> answerlist) {
        return answerRepository.saveAll(answerlist);
    }


    @Override
    public Answer getAnswerById(int id) throws ResourceNotFroundException {
        Answer answer = answerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Answer not exist with id: " + id));
        return answer;
    }

    @Override
    public Answer updateAnswer(int id, Answer answer) throws ResourceNotFroundException {
        Answer answer_new = answerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Answer not exist with id: " + id));
        answer_new.setContent(answer.getContent());
        answer_new.set_true(answer.is_true());
        answer_new.setPicture(answer.getPicture());
        answer_new.setAudio(answer.getAudio());
        answer_new.setVideo(answer.getVideo());
        return answerRepository.save(answer_new);
    }

    @Override
    public Boolean deleteAnswer(int id) throws ResourceNotFroundException {
        Answer answer = answerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Answer not exist with id: " + id));
        answerRepository.delete(answer);
        return true;
    }
    }
