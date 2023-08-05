package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Notification;
import com.capstone.project.model.Setting;

import java.util.List;

public interface SettingService {
    List<Setting> getAllSetting();

    Setting getSettingById(int id) throws ResourceNotFroundException;

    Setting createSetting(Setting setting);

    Setting updateSetting( Setting setting,  int id) throws ResourceNotFroundException;
    Boolean deleteSetting( int id) throws ResourceNotFroundException;
}
