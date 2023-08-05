package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.HistoryType;

import java.util.List;

public interface HistoryTypeService {

    List<HistoryType> getAll();

    HistoryType getById(int id) throws ResourceNotFroundException;
}
