package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Assignment;
import com.capstone.project.model.Class;
import com.capstone.project.model.User;
import com.capstone.project.repository.AssignmentRepository;
import com.capstone.project.repository.ClassRepository;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.impl.*;
import com.capstone.project.util.DateRangePicker;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassStatisticServiceTest {

    @Mock
    private DateRangePicker dateRangePicker;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private TestServiceImpl testService;

    @Mock
    private SubmissionServiceImpl submissionService;

    @Mock
    private PostServiceImpl postService;

    @Mock
    private AssignmentServiceImpl assignmentService;
    @Mock
    private ClassLearnerService classLearnerService;

    @InjectMocks
    private ClassStatisticServiceImpl classStatisticService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Order(1)
    @Test
    public void getTTestNumber() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(classRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Class.builder().build()));
            when(dateRangePicker.getDateActive()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(testService.getFilterTest(any(), any(), any(), anyInt(), anyInt(), any(), any(),
                    any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
            Integer result = classStatisticService.getTestNumber(1);
            assertThat(result).isEqualTo(2);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(2)
    @Test
    public void getTAssignmentNumber() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(classRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Class.builder().build()));
            when(dateRangePicker.getDateActive()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(assignmentService.getFilterAssignment(any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), anyInt(), anyInt(), anyInt())).thenReturn(response);
            Integer result = classStatisticService.getAssignmentNumber(1);
            assertThat(result).isEqualTo(2);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @Test
    public void getTLearnerJoinedNumber() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(classRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Class.builder().build()));
            when(dateRangePicker.getDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(classLearnerService.filterClassLearner(anyInt(), anyInt(), any(),any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
            Integer result = classStatisticService.getLeanerJoinedNumber(1);
            assertThat(result).isEqualTo(2);
        } catch (ResourceNotFroundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(4)
    @Test
    public void getLearnerJoinedGrowth() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(classRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Class.builder().build()));
            when(dateRangePicker.getShortDateRange()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(classLearnerService.filterClassLearner(anyInt(), anyInt(), any(),any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(response);
            List<Integer> result = classStatisticService.getLeanerJoinedGrowth(1);
            assertThat(result.size()).isGreaterThan(0);
        } catch (ResourceNotFroundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(5)
    @Test
    public void getPostGrowth() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 2);
            when(dateRangePicker.getDateActive()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(postService.getFilterPost(any(), any(), any(), any(), any(), any(), anyInt(), anyInt(), anyInt())).thenReturn(response);
            List<Integer> result = classStatisticService.getPostGrowth(1);
            assertThat(result.size()).isGreaterThan(0);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(6)
    @Test
    public void getPointDistribution() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", 1); // Đặt totalItems là 1 để giả lập danh sách có một phần tử
            when(assignmentRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Assignment.builder().build()));
            when(dateRangePicker.getDateActive()).thenReturn(Arrays.asList("2023-01-01", "2023-02-01"));
            when(submissionService.getFilterSubmission(any(), anyInt(), anyInt(), anyInt(), any(), any(), any(), anyInt(), anyInt()))
                    .thenReturn(response);
            List<Integer> result = classStatisticService.getPointDistribution(2);
            assertThat(result.size()).isGreaterThan(0);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

}
