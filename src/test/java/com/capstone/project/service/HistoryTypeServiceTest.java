package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.AchievementType;
import com.capstone.project.model.HistoryType;
import com.capstone.project.repository.HistoryTypeRepository;
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
public class HistoryTypeServiceTest {

    @Mock
    private HistoryTypeRepository historyTypeRepository;

    @InjectMocks
    private HistoryTypeServiceImpl historyTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getHistoryTypes() {
        HistoryType historyType = HistoryType.builder()
                .name("stub")
                .build();

        List<HistoryType> historyTypes = List.of(historyType);
        when(historyTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(historyTypes);

        assertThat(historyTypeService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getHistoryTypeById() {
        HistoryType historyType = HistoryType.builder().name("stub").build();
        when(historyTypeRepository.findById(any())).thenReturn(Optional.ofNullable(historyType));
        try {
            HistoryType getHistoryType = historyTypeService.getById(1);
            assertThat(getHistoryType).isEqualTo(historyType);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }
}
