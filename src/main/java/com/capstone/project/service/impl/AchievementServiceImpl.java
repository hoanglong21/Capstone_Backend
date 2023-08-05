package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Achievement;
import com.capstone.project.repository.AchievementRepository;
import com.capstone.project.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementServiceImpl(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Override
    public List<Achievement> getAll() {
        return achievementRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Achievement getById(int id) throws ResourceNotFroundException {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Achievement not exist with id: " + id));
        return achievement;
    }
}
