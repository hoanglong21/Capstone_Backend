package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.impl.OverviewStatisticServiceImpl;
import com.capstone.project.util.DateRangePicker;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OverviewStatisticServiceTest {

    @InjectMocks
    private OverviewStatisticServiceImpl overviewStatisticService;

    @Mock
    private StudySetService studySetService;

    @Mock
    private DateRangePicker dateRangePicker;

    @Mock
    private UserService userService;

    @Mock
    private HistoryService historyService;

    @Mock
    private ClassService classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getUserGrowth() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        when(dateRangePicker.getDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
        when(userService.filterUser(any(),any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                anyInt(), anyInt())).thenReturn(response);
        List<Integer> list = overviewStatisticService.getUserGrowth();
        assertThat(list.size()).isEqualTo(1);
    }

    @Order(2)
    @Test
    public void getStudySetGrowth() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        when(dateRangePicker.getDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
        when(studySetService.getFilterList(any(), any(), any(), any(), anyInt(), anyInt(), any(),
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
        List<Integer> list = overviewStatisticService.getStudySetGrowth();
        assertThat(list.size()).isEqualTo(1);
    }

    @Order(3)
    @Test
    public void getAccessNumber() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
        when(historyService.filterHistory(anyInt(), anyInt(), anyInt(), anyInt(), any(), any(),
                any(), any(), anyInt(), anyInt())).thenReturn(response);
        Integer result = overviewStatisticService.getAccessNumber();
        assertThat(result).isEqualTo(2);
    }

    @Order(4)
    @Test
    public void getRegisterNumber() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
        when(userService.filterUser(any(),any(), any(), any(), any(),
                any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), anyInt(), anyInt())).thenReturn(response);
        Integer result = overviewStatisticService.getRegisterNumber();
        assertThat(result).isEqualTo(2);
    }

    @Order(5)
    @Test
    public void getClassNumber() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(classService.getFilterClass(anyInt(), any(), any(), any(), any(),
                    any(), any(),any(),any(), any(), anyInt(), anyInt())).thenReturn(response);
            Integer result = overviewStatisticService.getClassNumber();
            assertThat(result).isEqualTo(2);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(6)
    @Test
    public void getStudySetNumber() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
        when(studySetService.getFilterList(any(), any(), any(), any(), anyInt(), anyInt(), any(), any(),
                any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
        Integer result = overviewStatisticService.getStudySetNumber();
        assertThat(result).isEqualTo(2);
    }
}
