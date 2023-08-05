package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.History;

import java.util.List;
import java.util.Map;

public interface HistoryService {

    List<History> getAll();

    History createHistory(History history);

    History getHistoryById(int id) throws ResourceNotFroundException;

    Map<String, Object> filterHistory(int userId, int destinationId, int typeId, int categoryId, String fromDatetime, String toDatetime,
                                      String sortBy, String direction, int page, int size);
}
