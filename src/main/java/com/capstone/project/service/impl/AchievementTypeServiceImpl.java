package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.AchievementType;
import com.capstone.project.repository.AchievementTypeRepository;
import com.capstone.project.service.AchievementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementTypeServiceImpl implements AchievementTypeService {

    private final AchievementTypeRepository achievementTypeRepository;

    @Autowired
    public AchievementTypeServiceImpl(AchievementTypeRepository achievementTypeRepository) {
        this.achievementTypeRepository = achievementTypeRepository;
    }

    @Override
    public List<AchievementType> getAll() {
        return achievementTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public AchievementType getById(int id) throws ResourceNotFroundException {
        AchievementType achievementType = achievementTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Achievement Type not exist with id: " + id));
        return achievementType;
    }
}
