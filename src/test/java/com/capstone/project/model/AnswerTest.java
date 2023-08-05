package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {

    @ParameterizedTest(name = "index => questionId={0}, content={1},istrue{2}, picture={3}, audio{4}, video{5}")
    @CsvSource({
            "1, Knight, true, knight.jpg, knight.mp3, knight.mp4 ",
            "2, Cat , false, cat.jpg, cat.mp3, cat.mp4 "
    })
    public void testAnswer(int questionId, String content,Boolean istrue,String picture, String audio, String video){
        Answer answer = Answer.builder()
                .question(Question.builder().id(questionId).build())
                .content(content)
                .is_true(istrue)
                .picture(picture)
                .audio(audio)
                .video(video)
                .build();
        assertThat(answer).isNotNull();
        assertThat(answer.getQuestion().getId()).isEqualTo(questionId);
        assertThat(answer.getContent()).isEqualTo(content);
        assertThat(answer.is_true()).isEqualTo(istrue);
        assertThat(answer.getPicture()).isEqualTo(picture);
        assertThat(answer.getAudio()).isEqualTo(audio);
        assertThat(answer.getVideo()).isEqualTo(video);
    }
}
