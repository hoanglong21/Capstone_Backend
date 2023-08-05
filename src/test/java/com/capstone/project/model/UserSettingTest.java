package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserSettingTest {

    @ParameterizedTest(name = "index => userId={0}, settingId{1}, value{2}")
    @CsvSource({
            "1,2,ja ",
            "1,4,15:00 "
    })
    public void testUserSetting(int userId, int settingId, String value){
        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(userId).build())
                .setting(Setting.builder().id(settingId).build())
                .value(value)
                .build();
        assertThat(userSetting).isNotNull();
        assertThat(userSetting.getUser().getId()).isEqualTo(userId);
        assertThat(userSetting.getSetting().getId()).isEqualTo(settingId);
        assertThat(userSetting.getValue()).isEqualTo(value);
    }
}
