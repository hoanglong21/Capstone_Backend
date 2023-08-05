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
public class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetQuestionByTestId(Boolean trueId){

        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Test test = Test.builder().description("Test for all").classroom(classroom).duration(12).title("Progress test").user(user).build();
        testRepository.save(test);

        Question question = Question.builder()
                .test(test)
                .num_choice(3)
                .question("Who kill Jack Robin")
                .questionType(QuestionType.builder().id(1).build())
                .build();
          questionRepository.save(question);

        if(trueId) {
            List<Question> result = questionRepository.getQuestionByTestId(test.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Question> result = questionRepository.getQuestionByTestId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
