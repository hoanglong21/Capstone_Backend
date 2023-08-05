package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.FeedbackType;
import com.capstone.project.model.HistoryType;
import com.capstone.project.repository.FeedbackTypeRepository;
import com.capstone.project.repository.HistoryTypeRepository;
import com.capstone.project.service.impl.FeedbackTypeServiceImpl;
import com.capstone.project.service.impl.HistoryTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedbackTypeServiceTest {

    @Mock
    private FeedbackTypeRepository feedbackTypeRepository;

    @InjectMocks
    private FeedbackTypeServiceImpl feedbackTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getHistoryTypes() {
        FeedbackType feedbackType = FeedbackType.builder()
                .name("stub")
                .build();

        List<FeedbackType> historyTypes = List.of(feedbackType);
        when(feedbackTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(historyTypes);

        assertThat(feedbackTypeService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getHistoryTypeById() {
        FeedbackType feedbackType = FeedbackType.builder().name("stub").build();
        when(feedbackTypeRepository.findById(any())).thenReturn(Optional.ofNullable(feedbackType));
        try {
            FeedbackType getFeedbackType = feedbackTypeService.getById(1);
            assertThat(getFeedbackType).isEqualTo(feedbackType);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }
}
