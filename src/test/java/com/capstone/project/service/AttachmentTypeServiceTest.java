package com.capstone.project.service;

import com.capstone.project.model.AttachmentType;
import com.capstone.project.repository.AttachmentTypeRepository;
import com.capstone.project.service.impl.AttachmentTypeServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttachmentTypeServiceTest {

    @Mock
    private AttachmentTypeRepository attachmentTypeRepository;
    @InjectMocks
    private AttachmentTypeServiceImpl attachmentTypeService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void testGetAllAchievementType() {
       AttachmentType attachmentType = AttachmentType.builder()
                .name("stub")
                .build();

        List<AttachmentType> attachmentTypes = Arrays.asList(attachmentType);
        when(attachmentTypeRepository.findAll()).thenReturn(attachmentTypes);

        assertThat(attachmentTypeService.getAllType().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getAttachmentTypeById() {
        AttachmentType attachmentType = AttachmentType.builder()
                .name("stub")
                .build();
        when(attachmentTypeRepository.findById(any())).thenReturn(Optional.ofNullable(attachmentType));
            AttachmentType attachmentTypes = attachmentTypeService.getAttachmentTypeById(1);
            assertThat(attachmentTypes).isEqualTo(attachmentType);

    }
}
