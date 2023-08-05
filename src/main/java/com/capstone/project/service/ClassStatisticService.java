package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;

import java.text.ParseException;
import java.util.List;

public interface ClassStatisticService {
    Integer getTestNumber(int id) throws ResourceNotFroundException;

    Integer getAssignmentNumber(int id) throws ResourceNotFroundException;

    Integer getLeanerJoinedNumber(int id) throws ResourceNotFroundException, ParseException;

    List<Integer> getLeanerJoinedGrowth(int id) throws ResourceNotFroundException, ParseException;
    List<Integer> getPostGrowth(int id) throws ResourceNotFroundException;

    List<Integer> getPointDistribution(int id) throws ResourceNotFroundException;
}
