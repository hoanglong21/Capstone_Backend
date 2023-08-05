package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardTest {
    @ParameterizedTest(name = "index => picture={0}, audio={1}, studySetId={2}")
    @CsvSource({
            "picture1, audio1, 1",
            "picture2, audio2, 2"
    })
    public void testCard(String picture, String audio, int studySetId) {
        Card card = Card.builder()
                .picture(picture)
                .audio(audio)
                .studySet(StudySet.builder().id(studySetId).build())
                .build();

        assertThat(card).isNotNull();
        assertThat(card.getPicture()).isEqualTo(picture);
        assertThat(card.getAudio()).isEqualTo(audio);
        assertThat(card.getStudySet().getId()).isEqualTo(studySetId);
    }
}
