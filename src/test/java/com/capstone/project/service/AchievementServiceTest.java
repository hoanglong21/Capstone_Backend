package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Achievement;
import com.capstone.project.model.Feedback;
import com.capstone.project.repository.AchievementRepository;
import com.capstone.project.service.impl.AchievementServiceImpl;
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
public class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAchievement() {
        Achievement achievement = Achievement.builder()
                .name("stub")
                .build();

        List<Achievement> achievements = List.of(achievement);
        when(achievementRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(achievements);

        assertThat(achievementService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getAchievementById() {
        Achievement achievement = Achievement.builder().name("stub").build();
        when(achievementRepository.findById(any())).thenReturn(Optional.ofNullable(achievement));
        try {
            Achievement getAchievement = achievementService.getById(1);
            assertThat(getAchievement).isEqualTo(achievement);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }
}
