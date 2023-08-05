package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.UserSetting;

import java.util.List;
import java.util.Map;

public interface UserSettingService {

    List<UserSetting> getAllUserSetting();

    UserSetting getUserSettingById(int id) throws ResourceNotFroundException;
    List<UserSetting> getUserSettingBySettingId(int id);
    List<UserSetting> getUserSettingByUserId(int id);

    UserSetting createUserSetting(UserSetting usersetting);

    UserSetting updateUserSetting( UserSetting usersetting,  int id) throws ResourceNotFroundException;
    Boolean deleteUserSetting( int id) throws ResourceNotFroundException;

    Map<String, String> CustomGetUserSettingByUserId(int id) throws ResourceNotFroundException;

    UserSetting saveUserSettingCustom(int userId, int settingId, String newValue);

    void sendStudyReminderMails();
}
