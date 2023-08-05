package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.HistoryType;
import com.capstone.project.repository.HistoryTypeRepository;
import com.capstone.project.service.HistoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryTypeServiceImpl implements HistoryTypeService {
    private final HistoryTypeRepository historyTypeRepository;

    @Autowired
    public HistoryTypeServiceImpl(HistoryTypeRepository historyTypeRepository) {
        this.historyTypeRepository = historyTypeRepository;
    }

    @Override
    public List<HistoryType> getAll() {
        return historyTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public HistoryType getById(int id) throws ResourceNotFroundException {
        HistoryType historyType = historyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("History Type not exist with id: " + id));
        return historyType;
    }
}
