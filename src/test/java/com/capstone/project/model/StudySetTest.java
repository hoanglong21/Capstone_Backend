package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudySetTest {
    @ParameterizedTest(name = "index => userId={0}, studySetTypeId={1}, title={2}, description{3}, isDeleted={4}, isPublic={5}, isDraft={6}")
    @CsvSource({
            "1, 1, 1, Title1, Description1, true, false, true",
            "2, 2, 2, Title2, Description2, false, true, false"
    })
    public void testStudySet(int userId, int studySetTypeId, String title, String description, boolean isDeleted, boolean isPublic, boolean isDraft) {
        StudySet studySet = StudySet.builder()
                .user(User.builder().id(userId).build())
                .studySetType(StudySetType.builder().id(studySetTypeId).build())
                .title(title)
                .description(description)
                .is_deleted(isDeleted)
                .is_public(isPublic)
                .is_draft(isDraft)
                .build();

        assertThat(studySet).isNotNull();
        assertThat(studySet.getUser().getId()).isEqualTo(userId);
        assertThat(studySet.getStudySetType().getId()).isEqualTo(studySetTypeId);
        assertThat(studySet.getTitle()).isEqualTo(title);
        assertThat(studySet.getDescription()).isEqualTo(description);
        assertThat(studySet.is_deleted()).isEqualTo(isDeleted);
        assertThat(studySet.is_public()).isEqualTo(isPublic);
        assertThat(studySet.is_draft()).isEqualTo(isDraft);
    }
}
