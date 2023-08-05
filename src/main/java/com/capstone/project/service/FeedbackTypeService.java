package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.FeedbackType;

import java.util.List;

public interface FeedbackTypeService {
    List<FeedbackType> getAll();

    FeedbackType getById(int id) throws ResourceNotFroundException;
}
