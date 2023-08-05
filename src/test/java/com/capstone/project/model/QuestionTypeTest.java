package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTypeTest {

    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "QuestionType1",
            "QuestionType2"
    })
    public void testQuestion(String name){
        QuestionType questionType = QuestionType.builder()
                .name(name).build();
        assertThat(questionType).isNotNull();
        assertThat(questionType.getName()).isEqualTo(name);
    }
}
