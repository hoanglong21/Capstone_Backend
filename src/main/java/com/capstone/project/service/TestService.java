package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Post;
import com.capstone.project.model.Test;
import com.capstone.project.model.TestLearner;
import com.capstone.project.model.TestResult;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TestService {
    List<Test> getAllTest();

    Test createTest(Test test);

    Test getTestById ( int id) throws ResourceNotFroundException;

    List<Test> getAllTestByClassId(int id);
    List<Test> getTestByUser (String name) throws ResourceNotFroundException;

    Test updateTest( int id, Test test) throws ResourceNotFroundException;

    Boolean deleteTest( int id) throws ResourceNotFroundException;

    Map<String, Object> getFilterTest(String search, String author, String direction, int duration, int classid,
                                      String fromStarted, String toStarted, String fromCreated, String toCreated, Boolean isDraft, String sortBy, int page, int size) throws ResourceNotFroundException;

    TestLearner startTest(int testId, int userId);
    double endTest(List<TestResult> testResultList) throws ResourceNotFroundException;
}
