package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AchievementTypeTest {
    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "AchievementType1",
            "AchievementType2"
    })
    public void testAchievementType(String name) {
        AchievementType achievementType = AchievementType.builder().name(name).build();

        assertThat(achievementType).isNotNull();
        assertThat(achievementType.getName()).isEqualTo(name);
    }
}
