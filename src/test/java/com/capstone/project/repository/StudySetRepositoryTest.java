package com.capstone.project.repository;
import com.capstone.project.model.*;
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
public class StudySetRepositoryTest {

    @Autowired
    private StudySetRepository studySetRepository;

    @Autowired
    private UserRepository userRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testFindStudySetByAuthor_id(boolean trueId) {
        // create stub
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        StudySet studySet = StudySet.builder()
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .user(user).build();
        studySetRepository.save(studySet);
        // end of create stub

        if(trueId) {
            List<StudySet> result = studySetRepository.findStudySetByAuthor_id(user.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<StudySet> result = studySetRepository.findStudySetByAuthor_id(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
