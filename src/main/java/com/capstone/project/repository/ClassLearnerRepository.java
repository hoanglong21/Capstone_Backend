package com.capstone.project.repository;

import com.capstone.project.model.Class;
import com.capstone.project.model.ClassLearner;
import com.capstone.project.model.User;
import com.capstone.project.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassLearnerRepository extends JpaRepository<ClassLearner,Integer> {
    ClassLearner findByUserIdAndClassroomId(int user,int classroom);

    ClassLearner getClassLeanerByUserId(int id);
}
