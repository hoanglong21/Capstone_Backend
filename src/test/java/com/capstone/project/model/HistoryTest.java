package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HistoryTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "index => typeId={0}, studySetId={1}, classId={2}, userId={3}, datetime={4}")
    @CsvSource({
            "1, 0, 0, 1, 2023-7-1",
            "2, 1, 0, 1, 2023-7-1",
            "3, 0, 1, 1, 2023-7-1"
    })
    public void testHistory(int typeId, int studySetId, int classId, int userId, String datetime) {
        try {
            History history = History.builder()
                    .historyType(HistoryType.builder().id(typeId).build())
                    .user(User.builder().id(userId).build())
                    .studySet(StudySet.builder().id(studySetId).build())
                    .classroom(Class.builder().id(classId).build())
                    .datetime(dateFormat.parse(datetime))
                    .build();

            assertThat(history).isNotNull();
            assertThat(history.getHistoryType().getId()).isEqualTo(typeId);
            assertThat(history.getUser().getId()).isEqualTo(userId);
            assertThat(history.getStudySet().getId()).isEqualTo(studySetId);
            assertThat(history.getClassroom().getId()).isEqualTo(classId);
            assertThat(history.getDatetime()).isEqualTo(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
