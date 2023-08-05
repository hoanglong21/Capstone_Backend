package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AchievementTest {
    @ParameterizedTest(name = "index => achievementTypeId={0}, name={1}, description={2}")
    @CsvSource({
            "1, Achievement1, Description1",
            "2, Achievement2, Description2"
    })
    public void testAchievement(int achievementTypeId, String name, String description) {
        Achievement achievement = Achievement.builder()
                .achievementType(AchievementType.builder().id(achievementTypeId).build())
                .name(name)
                .description(description)
                .build();

        assertThat(achievement).isNotNull();
        assertThat(achievement.getAchievementType().getId()).isEqualTo(achievementTypeId);
        assertThat(achievement.getName()).isEqualTo(name);
        assertThat(achievement.getDescription()).isEqualTo(description);
    }
}
