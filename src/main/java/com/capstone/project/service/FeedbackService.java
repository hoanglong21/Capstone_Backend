package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Feedback;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface FeedbackService {

    List<Feedback> getAllFeedbacks();

    Feedback createFeedback(Feedback feedback);

    Feedback getFeedbackById(int id) throws ResourceNotFroundException;

    Feedback updateFeedback(int id, Feedback feedbackDetails) throws ResourceNotFroundException;

    Boolean deleteFeedback(int id) throws ResourceNotFroundException;

    Map<String, Object> filterFeedback(String search, int type, int authorId, String authorName, String destination, String fromCreated, String toCreated,
                                       String sortBy, String direction, int page, int size) throws ParseException;

    String replyFeedback(int feedbackId, String title, String content) throws Exception;
}
