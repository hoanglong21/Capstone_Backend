package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.AnswerRepository;
import com.capstone.project.repository.QuestionRepository;
import com.capstone.project.service.QuestionService;
import com.capstone.project.service.QuestionTypeService;
import com.capstone.project.service.TestService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @PersistenceContext
    private EntityManager em;
    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;
    private final QuestionTypeService questionTypeService;
    private final TestService testService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionTypeService questionTypeService, TestService testService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionTypeService = questionTypeService;
        this.testService = testService;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getAllByTestId(int id) {
        return questionRepository.getQuestionByTestId(id);
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> createQuestions(List<Question> questionlist) {
        return questionRepository.saveAll(questionlist);
    }


    @Override
    public Question getQuestionById(int id) throws ResourceNotFroundException {
        Question question = questionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Question not exist with id: " + id));
        return question;
    }

    @Override
    public Question updateQuestion(int id, Question question) throws ResourceNotFroundException {
        Question question_new = questionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Question not exist with id: " + id));
        question_new.setNum_choice(question.getNum_choice());
        question_new.setQuestion(question.getQuestion());
        question_new.setQuestionType(question.getQuestionType());
        question_new.setPicture(question.getPicture());
        question_new.setAudio(question.getAudio());
        question_new.setVideo(question.getVideo());
        question_new.setPoint(question.getPoint());
        return questionRepository.save(question_new);
    }

    @Override
    public Boolean deleteQuestion(int id) throws ResourceNotFroundException {
        Question question  = questionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Question not exist with id:" + id));
        for (Answer answer : answerRepository.getAnswerByQuestionId(question.getId())) {
             answerRepository.delete(answer);
        }
        questionRepository.delete(question);
        return true;
    }

    @Override
    public Map<String, Object> getFilterQuestion(String search, int typeid, int testid, int page, int size) throws ResourceNotFroundException {
        int offset = (page - 1) * size;

        String query ="SELECT * FROM question WHERE 1=1";

        Map<String, Object> parameters = new HashMap<>();


        if (search != null && !search.isEmpty()) {
            query += " AND question LIKE :search ";
            parameters.put("search", "%" + search + "%");
        }

        if (typeid != 0) {
            query += " AND type_id = :typeId";
            parameters.put("typeId", typeid);
        }

        if (testid != 0) {
            query += " AND test_id = :testId";
            parameters.put("testId", testid);
        }


        Query q = em.createNativeQuery(query, Question.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<Question> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;
    }
}
