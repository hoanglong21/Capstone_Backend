package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Achievement;
import com.capstone.project.model.AchievementType;
import com.capstone.project.repository.AchievementRepository;
import com.capstone.project.repository.AchievementTypeRepository;
import com.capstone.project.service.impl.AchievementServiceImpl;
import com.capstone.project.service.impl.AchievementTypeServiceImpl;
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
public class AchievementTypeServiceTest {
    @Mock
    private AchievementTypeRepository achievementTypeRepository;

    @InjectMocks
    private AchievementTypeServiceImpl achievementTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAchievementType() {
        AchievementType achievementType = AchievementType.builder()
                .name("stub")
                .build();

        List<AchievementType> achievementTypes = List.of(achievementType);
        when(achievementTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(achievementTypes);

        assertThat(achievementTypeService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getAchievementTypeById() {
        AchievementType achievementType = AchievementType.builder().name("stub").build();
        when(achievementTypeRepository.findById(any())).thenReturn(Optional.ofNullable(achievementType));
        try {
            AchievementType getAchievementType = achievementTypeService.getById(1);
            assertThat(getAchievementType).isEqualTo(achievementType);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }
}
