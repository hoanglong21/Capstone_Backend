package com.capstone.project.repository;
import com.capstone.project.model.*;
import com.capstone.project.repository.CardRepository;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.repository.StudySetRepository;
import com.capstone.project.repository.UserRepository;
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
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private UserRepository userRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getCardByStudySetId(boolean trueId) {
        // create stub
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        StudySet studySet = StudySet.builder().title("Stub").studySetType(StudySetType.builder().id(1).build()).user(user).build();
        studySetRepository.save(studySet);

        Card card = Card.builder()
                .audio("audio")
                .picture("picture")
                .studySet(studySet)
                .build();
        cardRepository.save(card);
        // end of create stub

        if(trueId) {
            List<Card> result = cardRepository.getCardByStudySetId(studySet.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Card> result = cardRepository.getCardByStudySetId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
