package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Field;
import com.capstone.project.model.History;
import com.capstone.project.model.HistoryType;
import com.capstone.project.model.User;
import com.capstone.project.repository.FieldRepository;
import com.capstone.project.service.impl.FieldServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FieldServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAll() {
        Field field = Field.builder()
                .name("stub")
                .build();

        List<Field> histories = List.of(field);
        when(fieldRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(histories);

        assertThat(fieldService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getFieldServiceId() {
        Field field = Field.builder().name("stub").build();
        when(fieldRepository.findById(any())).thenReturn(Optional.ofNullable(field));
        try {
            Field getField = fieldService.getFieldById(1);
            assertThat(getField).isEqualTo(field);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @Test
    public void getFieldsByStudySetTypeId() {
        Field field = Field.builder().name("stub").build();
        List<Field> histories = List.of(field);
        when(fieldRepository.findFieldsByType_Id(anyInt())).thenReturn(histories);

        assertThat(fieldService.getFieldsByStudySetTypeId(1).size()).isGreaterThan(0);
    }
}
