package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.UserAchievement;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserAchievementService {

    List<UserAchievement> getAll();

    UserAchievement createUserAchievement(UserAchievement userAchievement);

    UserAchievement getUserAchievementById(int id) throws ResourceNotFroundException;

    List<UserAchievement> getUserAchievementByUserId(int id);

    Map<String, Object> filterUserAchievement(int userId, int achievementId, String fromDatetime, String toDatetime,
                                             String sortBy, String direction, int page, int size) throws ParseException;

}
