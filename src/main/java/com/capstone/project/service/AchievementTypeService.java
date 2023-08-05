package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.AchievementType;

import java.util.List;

public interface AchievementTypeService {

    List<AchievementType> getAll();

    AchievementType getById(int id) throws ResourceNotFroundException;
}
