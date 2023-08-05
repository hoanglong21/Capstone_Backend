package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Field;
import com.capstone.project.model.StudySetType;
import com.capstone.project.repository.FieldRepository;
import com.capstone.project.repository.StudySetTypeRepository;
import com.capstone.project.service.impl.FieldServiceImpl;
import com.capstone.project.service.impl.StudySetTypeServiceImpl;
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
public class StudySetTypeServiceTest {

    @Mock
    private StudySetTypeRepository studySetTypeRepository;

    @InjectMocks
    private StudySetTypeServiceImpl studySetTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAll() {
        StudySetType studySetType = StudySetType.builder()
                .name("stub")
                .build();

        List<StudySetType> studySetTypes = List.of(studySetType);
        when(studySetTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(studySetTypes);

        assertThat(studySetTypeService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getStudySetTypeById() {
        StudySetType studySetType = StudySetType.builder().name("stub").build();
        when(studySetTypeRepository.findById(any())).thenReturn(Optional.ofNullable(studySetType));
        try {
            StudySetType getStudySetType = studySetTypeService.getStudySetTypeById(1);
            assertThat(getStudySetType).isEqualTo(studySetType);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }
}
