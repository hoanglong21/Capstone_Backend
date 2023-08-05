package com.capstone.project.repository;
import com.capstone.project.model.Comment;
import com.capstone.project.model.Setting;
import com.capstone.project.model.User;
import com.capstone.project.model.UserSetting;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSettingRepositoryTest {
    @Autowired
    private UserSettingRepository userSettingRepository;


    @Order(1)
    @ParameterizedTest(name = "index => userId={0}, settingId{1}, value{2}")
    @CsvSource({
            "2, 2, ja",
            "3,3, vi",
    })
    public void testGetByUserId(int userId, int settingId, String value) {
        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(userId).build())
                .setting(Setting.builder().id(settingId).build())
                .value(value)
                .build();
        userSettingRepository.save(userSetting);

        List<UserSetting> userSettings = userSettingRepository.getByUserId(userId);
        assertThat(userSettings.size()).isGreaterThan(0);

    }

    @Order(2)
    @ParameterizedTest(name = "index => userId={0}, settingId{1}, value{2}")
    @CsvSource({
            "4, 2, ja",
            "3,3, vi",
    })
    public void testGetBySettingId(int userId, int settingId, String value) {
        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(userId).build())
                .setting(Setting.builder().id(settingId).build())
                .value(value)
                .build();
        userSettingRepository.save(userSetting);

        List<UserSetting> userSettings = userSettingRepository.getBySettingId(settingId);
        assertThat(userSettings.size()).isGreaterThan(0);

    }

    @Order(3)
    @ParameterizedTest(name = "{index} => userId={0}, settingId{1}, value{2}" )
    @CsvSource({
            "3, 2, ja",
            "4,2, vi",
    })
    public void testGetUserSettingCustom(int userId,int settingId,String value)  {
        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(userId).build())
                .setting(Setting.builder().id(settingId).build())
                .value(value)
                .build();
        userSettingRepository.save(userSetting);
            UserSetting userSettings = userSettingRepository.getUserSettingCustom(userId,settingId);
            assertThat(userSettings).isNotNull();


    }

}
