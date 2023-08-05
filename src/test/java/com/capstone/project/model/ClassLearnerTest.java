package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClassLearnerTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0},classId{1}, created_date{2}")
    @CsvSource({
            "1,1,2023-4-7 ",
            "2,1,2023-4-8 "
    })
    public void testClassLearner(int userId,int classId,String created_date) {
        try {
            ClassLearner classLearner = ClassLearner.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classId).build())
                    .created_date(dateFormat.parse(created_date)).build();

            assertThat(classLearner).isNotNull();
            assertThat(classLearner.getUser().getId()).isEqualTo(userId);
            assertThat(classLearner.getClassroom().getId()).isEqualTo(classId);
            assertThat(classLearner.getCreated_date()).isEqualTo(created_date);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

    }
}
