package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeedbackTypeTest {
    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "FeedbackType1",
            "FeedbackType2"
    })
    public void testFeedbackType(String name) {
        FeedbackType feedbackType = FeedbackType.builder().name(name).build();

        assertThat(feedbackType).isNotNull();
        assertThat(feedbackType.getName()).isEqualTo(name);
    }
}
