package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.StudySetType;

import java.util.List;

public interface StudySetTypeService {

    StudySetType getStudySetTypeById (int id) throws ResourceNotFroundException;

    List<StudySetType> getAll();
}
