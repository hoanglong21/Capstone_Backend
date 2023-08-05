package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SettingTest {

    @ParameterizedTest(name = "index => title={0}")
    @CsvSource({
            "Studyset reminder ",
            "Language "
    })
    public void testSetting(String title){

        Setting setting = Setting.builder()
                .title(title)
                .build();

        assertThat(setting.getTitle()).isEqualTo(title);
    }
}
