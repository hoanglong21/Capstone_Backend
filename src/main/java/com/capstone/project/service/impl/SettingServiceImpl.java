package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Setting;
import com.capstone.project.repository.SettingRepository;
import com.capstone.project.service.SettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public List<Setting> getAllSetting() {
        return settingRepository.findAll();
    }

    @Override
    public Setting getSettingById(int id) throws ResourceNotFroundException {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Setting not exist with id:" + id));
        return setting;
    }

    @Override
    public Setting createSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public Setting updateSetting(Setting setting, int id) throws ResourceNotFroundException {
        Setting settings = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Setting not exist with id:" + id));
        settings.setTitle(setting.getTitle());
        return settingRepository.save(settings);
    }

    @Override
    public Boolean deleteSetting(int id) throws ResourceNotFroundException {
        Setting settings = settingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Setting not exist with id:" + id));
        settingRepository.delete(settings);
        return true;
    }
}
