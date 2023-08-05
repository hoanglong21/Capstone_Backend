package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Achievement;
import com.capstone.project.model.AchievementType;

import java.util.List;

public interface AchievementService {

    List<Achievement> getAll();

    Achievement getById(int id) throws ResourceNotFroundException;
}
