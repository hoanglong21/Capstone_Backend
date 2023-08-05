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
public class FeedbackTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "index => userId={0}, feedbackTypeId={1}, title={2}, destination={3}, content={4}, createdDate={5}")
    @CsvSource({
            "1, 1, title1, user/1, user abc have something wrong, 2023-7-1",
            "2, 2, title2, system, you need to integration with GPT, 2023-7-1"
    })
    public void testFeedback(int userId, int feedbackTypeId, String title, String destination, String content, String createdDate) {
        try {
            Feedback feedback = Feedback.builder()
                    .user(User.builder().id(userId).build())
                    .feedbackType(FeedbackType.builder().id(feedbackTypeId).build())
                    .title(title)
                    .destination(destination)
                    .content(content)
                    .created_date(dateFormat.parse(createdDate))
                    .build();

            assertThat(feedback).isNotNull();
            assertThat(feedback.getUser().getId()).isEqualTo(userId);
            assertThat(feedback.getFeedbackType().getId()).isEqualTo(feedbackTypeId);
            assertThat(feedback.getTitle()).isEqualTo(title);
            assertThat(feedback.getDestination()).isEqualTo(destination);
            assertThat(feedback.getContent()).isEqualTo(content);
            assertThat(feedback.getCreated_date()).isEqualTo(createdDate);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
