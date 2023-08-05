package com.capstone.project.service;

import com.capstone.project.model.AttachmentType;
import com.capstone.project.model.QuestionType;
import com.capstone.project.repository.AttachmentTypeRepository;
import com.capstone.project.repository.QuestionTypeRepository;
import com.capstone.project.service.impl.AttachmentTypeServiceImpl;
import com.capstone.project.service.impl.QuestionTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionTypeServiceTest {

    @Mock
    private QuestionTypeRepository questionTypeRepository;
    @InjectMocks
    private QuestionTypeServiceImpl questionTypeService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getQuestionTypeById() {
        QuestionType questionType = QuestionType.builder()
                .name("stub")
                .build();
        when(questionTypeRepository.findById(any())).thenReturn(Optional.ofNullable(questionType));
        QuestionType questionTypes = questionTypeService.getQuestionTypeById(1);
        assertThat(questionTypes).isEqualTo(questionType);

    }
}
