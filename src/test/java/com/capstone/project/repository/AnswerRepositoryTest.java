package com.capstone.project.repository;

import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRepository testRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getAnswerByQuestionId(boolean trueId){

        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Test test = Test.builder().description("Test for all").classroom(classroom).duration(12).title("Progress test").user(user).build();
         testRepository.save(test);

         Question question = Question.builder().test(test).questionType(QuestionType.builder().id(1).build())
                 .question("who kill jack robin")
                 .num_choice(3)
                 .build();
         questionRepository.save(question);

        Answer answer = Answer.builder().question(question).is_true(true).content("Knight").build();
        answerRepository.save(answer);
        if(trueId) {
            List<Answer> result = answerRepository.getAnswerByQuestionId(question.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Answer> result = answerRepository.getAnswerByQuestionId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
