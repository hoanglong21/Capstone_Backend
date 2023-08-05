package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.QuestionType;
import com.capstone.project.repository.QuestionTypeRepository;
import com.capstone.project.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {

    private final QuestionTypeRepository questionTypeRepository;
    @Autowired
    public QuestionTypeServiceImpl(QuestionTypeRepository questionTypeRepository) {
        this.questionTypeRepository = questionTypeRepository;
    }

    @Override
    public QuestionType getQuestionTypeById(int id) {
        QuestionType questionType= null;
        try {
            questionType = questionTypeRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFroundException("QuestionType not exist with id: " + id))
                    ;
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        return questionType;
    }
}
