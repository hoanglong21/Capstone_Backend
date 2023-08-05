package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;

import java.util.List;

public interface OverviewStatisticService {
    List<Integer> getUserGrowth();

    List<Integer> getStudySetGrowth();

    Integer getAccessNumber();

    Integer getRegisterNumber();

    Integer getClassNumber() throws ResourceNotFroundException;

    Integer getStudySetNumber();
}
