package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.impl.OverviewStatisticServiceImpl;
import com.capstone.project.service.impl.UserStatisticServiceImpl;
import com.capstone.project.util.DateRangePicker;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserStatisticServiceTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private DateRangePicker dateRangePicker;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClassLearnerService classLearnerService;

    @InjectMocks
    private UserStatisticServiceImpl userStatisticService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAccessStatistic() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(User.builder().build()));
            when(dateRangePicker.getDateActive()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(historyService.filterHistory(anyInt(), anyInt(), anyInt(), anyInt(), any(), any(),
                    any(), any(), anyInt(), anyInt())).thenReturn(response);
            List<List<Map<String, Integer>>> list = userStatisticService.getAccessStatistic(1);
            assertThat(list.size()).isEqualTo(1);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(2)
    @Test
    public void getStudySetLearnedStatistic() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(User.builder().build()));
            when(dateRangePicker.getDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(historyService.filterHistory(anyInt(), anyInt(), anyInt(), anyInt(), any(), any(),
                    any(), any(), anyInt(), anyInt())).thenReturn(response);
            List<Integer> list = userStatisticService.getStudySetLearnedStatistic(1);
            assertThat(list.size()).isEqualTo(1);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @Test
    public void getClassJoinedStatistic() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(User.builder().build()));
            when(dateRangePicker.getDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(classLearnerService.filterClassLearner(anyInt(), anyInt(), any(), any(),
                    any(),any(), any(), anyInt(), anyInt())).thenReturn(response);
            List<Integer> list = userStatisticService.getClassJoinedStatistic(1);
            assertThat(list.size()).isEqualTo(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Order(4)
    @Test
    public void getLearningStatistic() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(User.builder().build()));
            when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(historyService.filterHistory(anyInt(), anyInt(), anyInt(), anyInt(), any(), any(),
                    any(), any(), anyInt(), anyInt())).thenReturn(response);
            List<Integer> list = userStatisticService.getLearningStatistic(1);
            assertThat(list.size()).isEqualTo(3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
