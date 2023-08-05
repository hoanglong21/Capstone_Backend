package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserAchievementTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "index => userId={0}, achievementId={1}, createdDate={2}")
    @CsvSource({
            "1, 1, 2023-7-1",
            "1, 2, 2023-7-2",

    })
    public void testUserAchievement(int userId, int achievementId, String createdDate) {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(User.builder().id(userId).build())
                    .achievement(Achievement.builder().id(achievementId).build())
                    .created_date(dateFormat.parse(createdDate))
                    .build();

            assertThat(userAchievement).isNotNull();
            assertThat(userAchievement.getUser().getId()).isEqualTo(userId);
            assertThat(userAchievement.getAchievement().getId()).isEqualTo(achievementId);
            assertThat(userAchievement.getCreated_date()).isEqualTo(createdDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
