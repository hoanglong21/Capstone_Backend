package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HistoryTypeTest {
    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "HistoryType1",
            "HistoryType2"
    })
    public void testHistoryType(String name) {
        HistoryType historyType = HistoryType.builder().name(name).build();

        assertThat(historyType).isNotNull();
        assertThat(historyType.getName()).isEqualTo(name);
    }
}
