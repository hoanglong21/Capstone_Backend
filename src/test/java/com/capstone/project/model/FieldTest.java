package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FieldTest {
    @ParameterizedTest(name = "index => studySetType={0}, name={1}")
    @CsvSource({
            "1, Field1",
            "2, Field2"
    })
    public void testField(int studySetType, String name) {
        Field field = new Field(studySetType, name);

        assertThat(field).isNotNull();
        assertThat(field.getStudySetType().getId()).isEqualTo(studySetType);
        assertThat(field.getName()).isEqualTo(name);
    }
}
