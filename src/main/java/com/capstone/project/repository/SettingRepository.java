package com.capstone.project.repository;

import com.capstone.project.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting,Integer> {
}
