package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.*;
import com.capstone.project.util.DateRangePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OverviewStatisticServiceImpl implements OverviewStatisticService {

    @Autowired
    private UserService userService;

    @Autowired
    private StudySetService studySetService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ClassService classService;

    @Autowired
    private DateRangePicker dateRangePicker;

    @Override
    public List<Integer> getUserGrowth() {
        List<Integer> result = new ArrayList<>();
        List<String> listDate = dateRangePicker.getDateRange();
        for(int i=0; i<listDate.size()-1; i++) {
            Map<String, Object> response = userService.filterUser(null,null, null, null, null,
                    new String[]{"ROLE_LEARNER", "ROLE_TUTOR"}, null, null, null,
                    null, null, null, null, null, null, listDate.get(i), listDate.get(i+1),
                    "created_date", "DESC", 1, 5);
            result.add(Integer.parseInt(String.valueOf(response.get("totalItems"))));
        }
        return result;
    }

    @Override
    public List<Integer> getStudySetGrowth() {
        List<Integer> result = new ArrayList<>();
        List<String> listDate = dateRangePicker.getDateRange();
        for(int i=0; i<listDate.size()-1; i++) {
            Map<String, Object> response = studySetService.getFilterList(null, null, null, null,
                    0, 0, null, null, null, listDate.get(i), listDate.get(i+1),
                    "created_date", "DESC", 1, 5);
            result.add(Integer.parseInt(String.valueOf(response.get("totalItems"))));
        }
        return result;
    }

    @Override
    public Integer getAccessNumber() {
        List<String> listDate = dateRangePicker.getShortDateRange();
        Map<String, Object> response = historyService.filterHistory(0, 0, 1, 0, listDate.get(0), listDate.get(1),
                "datetime", "DESC", 1, 5);
        return Integer.parseInt(String.valueOf(response.get("totalItems")));
    }

    @Override
    public Integer getRegisterNumber() {
        List<String> listDate = dateRangePicker.getShortDateRange();
        Map<String, Object> response = userService.filterUser(null,null, null, null, null,
                new String[]{"ROLE_LEARNER", "ROLE_TUTOR"}, null, null, null,
                null, null, null, null, null, null, listDate.get(0), listDate.get(1),
                "created_date", "DESC", 1, 5);
        return Integer.parseInt(String.valueOf(response.get("totalItems")));

    }

    @Override
    public Integer getClassNumber() throws ResourceNotFroundException {
        List<String> listDate = dateRangePicker.getShortDateRange();
        Map<String, Object> response = classService.getFilterClass(0, null, null, null, null,
                null, listDate.get(0), listDate.get(1),"created_date", "DESC", 1, 5);
        return Integer.parseInt(String.valueOf(response.get("totalItems")));
    }

    @Override
    public Integer getStudySetNumber() {
        List<String> listDate = dateRangePicker.getShortDateRange();
        Map<String, Object> response = studySetService.getFilterList(null, null, null, null, 0,
                0, null, null, null,
                listDate.get(0), listDate.get(1), "created_date", "DESC", 1, 5);
        return Integer.parseInt(String.valueOf(response.get("totalItems")));
    }


}
