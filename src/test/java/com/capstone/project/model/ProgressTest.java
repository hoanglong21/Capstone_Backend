package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProgressTest {
    @ParameterizedTest(name = "index => userId={0}, cardId={1}, status={2}, right={3}, wrong={4}, totalWrong={5}, isStar={6}, note={7}, picture={8}, audio={9}")
    @CsvSource({
            "1, 1, Still learning, 1, 0, 4, true, note1, picture1, audio1",
            "2, 2, Mastered, 3, 0, 0, false, note2, picture2, audio2"
    })
    public void testProgress(int userId, int cardId, String status, int right, int wrong, int totalWrong, boolean isStart, String note, String picture, String audio) {
        Progress progress = Progress.builder()
                .user(User.builder().id(userId).build())
                .card(Card.builder().id(cardId).build())
                .status(status)
                .right(right)
                .wrong(wrong)
                .total_wrong(totalWrong)
                .is_star(isStart)
                .note(note)
                .picture(picture)
                .audio(audio)
                .build();

        assertThat(progress).isNotNull();
        assertThat(progress.getUser().getId()).isEqualTo(userId);
        assertThat(progress.getCard().getId()).isEqualTo(cardId);
        assertThat(progress.getStatus()).isEqualTo(status);
        assertThat(progress.getRight()).isEqualTo(right);
        assertThat(progress.getWrong()).isEqualTo(wrong);
        assertThat(progress.is_star()).isEqualTo(isStart);
        assertThat(progress.getNote()).isEqualTo(note);
        assertThat(progress.getPicture()).isEqualTo(picture);
        assertThat(progress.getAudio()).isEqualTo(audio);
    }
}
