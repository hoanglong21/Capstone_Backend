package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Field;

import java.util.List;

public interface FieldService {

    Field getFieldById(int id) throws ResourceNotFroundException;

    List<Field> getAll();

    List<Field> getFieldsByStudySetTypeId(int id);
}
