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
public class TestTest {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "index => userId={0},classId{1}, title{2}, created_date{3},modified_date{4}, description{5},duration{6},is_Draft{7},num_attemps{8},start_date{9},due_date{9}")
    @CsvSource({
            "1,1,Test1,2023-4-5,2023-4-7, Test daily, 30,false,20, 2023-4-10,2023-4-10 ",
            "2,2,Test2,2023-4-5,2023-4-8, Midterm, 90, true, 20, 2023-4-15,2023-4-15 "
    })
    public void testTest(int userId,int classId,String title, String created_date, String modified_date,String description,int duration,boolean isDraft,int numattemps,String startDate,String dueDate) {
        try {
            Test test = Test.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classId).build())
                    .title(title)
                    .created_date(dateFormat.parse(created_date))
                    .modified_date(dateFormat.parse(modified_date))
                    .description(description)
                    .duration(duration)
                    .is_draft(isDraft)
                    .num_attemps(numattemps)
                    .start_date(dateFormat.parse(startDate))
                    .due_date(dateFormat.parse(dueDate))
                    .build();

            assertThat(test).isNotNull();
            assertThat(test.getUser().getId()).isEqualTo(userId);
            assertThat(test.getClassroom().getId()).isEqualTo(classId);
            assertThat(test.getTitle()).isEqualTo(title);
            assertThat(test.getCreated_date()).isEqualTo(created_date);
            assertThat(test.getModified_date()).isEqualTo(modified_date);
            assertThat(test.getDescription()).isEqualTo(description);
            assertThat(test.getDuration()).isEqualTo(duration);
            assertThat(test.is_draft()).isEqualTo(isDraft);
            assertThat(test.getNum_attemps()).isEqualTo(numattemps);
            assertThat(test.getStart_date()).isEqualTo(startDate);
            assertThat(test.getDue_date()).isEqualTo(dueDate);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
}
