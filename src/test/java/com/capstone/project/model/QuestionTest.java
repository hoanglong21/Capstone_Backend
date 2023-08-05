package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {
    @ParameterizedTest(name = "index => testId={0}, typeId={1}, num_choice{2}, content{3},picture{4}, audio{5}, video{6}, point{7}")
    @CsvSource({
            "1,1,2, Who kill Jack Robin,question1.jpg, question1.mp3, question1.mp4, 0.5 ",
            "2,2,3, Who is the first president, question2.jpg, question2.mp3, question2.mp4, 1.0"
    })
    public void testQuestion(int testId,int typeId, int num_choice,String content,String picture,String audio, String video,double point){
      Question question = Question.builder()
              .test(Test.builder().id(testId).build())
              .questionType(QuestionType.builder().id(typeId).build())
              .num_choice(num_choice)
              .question(content)
              .picture(picture)
              .audio(audio)
              .video(video)
              .point(point)
              .build();
      assertThat(question.getTest().getId()).isEqualTo(testId);
      assertThat(question.getQuestionType().getId()).isEqualTo(typeId);
      assertThat(question.getNum_choice()).isEqualTo(num_choice);
      assertThat(question.getQuestion()).isEqualTo(content);
      assertThat(question.getPicture()).isEqualTo(picture);
      assertThat(question.getAudio()).isEqualTo(audio);
      assertThat(question.getVideo()).isEqualTo(video);
      assertThat(question.getPoint()).isEqualTo(point);
    }
}
