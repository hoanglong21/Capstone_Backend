package com.capstone.project.repository;

import com.capstone.project.model.ClassLearner;
import com.capstone.project.model.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSettingRepository extends JpaRepository<UserSetting,Integer> {

    List<UserSetting> getByUserId(int id);
    List<UserSetting> getBySettingId(int id);

    @Query(value = "SELECT * FROM user_setting WHERE user_id = :userId AND setting_id = :settingId", nativeQuery = true)
    UserSetting getUserSettingCustom(int userId, int settingId);
}
