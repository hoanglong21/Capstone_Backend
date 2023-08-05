package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Comment;
import com.capstone.project.model.CommentType;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.repository.CommentTypeRepository;
import com.capstone.project.service.impl.CommentServiceImpl;
import com.capstone.project.service.impl.CommentTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentTypeServiceTest {

    @Mock
    private CommentTypeRepository commentTypeRepository;

    @InjectMocks
    private CommentTypeServiceImpl commentTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Order(1)
    @Test
    void testGetCommentTypeById() {

        CommentType commenttype = CommentType.builder()
                .name("post")
                .build();
        when(commentTypeRepository.findById(any())).thenReturn(Optional.ofNullable(commenttype));
        CommentType getCommenttype = commentTypeService.getCommentTypeById(1);
        assertThat(getCommenttype).isEqualTo(commenttype);
    }
}
