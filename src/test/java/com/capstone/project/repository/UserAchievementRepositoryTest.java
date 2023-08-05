package com.capstone.project.repository;

import com.capstone.project.model.Achievement;
import com.capstone.project.model.User;
import com.capstone.project.model.UserAchievement;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserAchievementRepositoryTest {
    @Autowired
    private UserAchievementRepository userAchievementRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "index => userId={0}, achievementId={1}, createdDate={2}")
    @CsvSource({
            "1, 1, 2023-7-1",
            "1, 5, 2023-7-2",
    })
    public void testGetUserAchievementByUserId(int userId, int achievementId, String createdDate) {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(User.builder().id(userId).build())
                    .achievement(Achievement.builder().id(achievementId).build())
                    .created_date(dateFormat.parse(createdDate))
                    .build();

            userAchievementRepository.save(userAchievement);

            // Test
            List<UserAchievement> userAchievements = userAchievementRepository.getUserAchievementByUserId(1);
            assertThat(userAchievements.size()).isGreaterThan(0);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
