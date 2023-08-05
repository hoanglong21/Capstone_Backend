package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Field;
import com.capstone.project.repository.FieldRepository;
import com.capstone.project.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public Field getFieldById(int id) throws ResourceNotFroundException {
        Field field = fieldRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("Field not exist with id: " + id));
        return field;
    }

    @Override
    public List<Field> getAll() {
        return fieldRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Field> getFieldsByStudySetTypeId(int id) {
        return fieldRepository.findFieldsByType_Id(id);
    }
}
