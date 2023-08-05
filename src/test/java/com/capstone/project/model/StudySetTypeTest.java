package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudySetTypeTest {
    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "StudySetType1",
            "StudySetType2"
    })
    public void testStudySetType(String name) {
        StudySetType studySetType = StudySetType.builder().name(name).build();

        assertThat(studySetType).isNotNull();
        assertThat(studySetType.getName()).isEqualTo(name);
    }
}
