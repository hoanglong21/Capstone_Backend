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
public class AssignmentTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0}, classId={1},created_date{2},instruction{3},due_date{4},modified_date{5},start_date{6} ,title{7}, is_Draft{8}")
    @CsvSource({
            "1,3,2023-8-9, Luyen thi JLPT N5,2023-8-12,2023-9-9,2023-8-10, On thi N3,true ",
            "2,4,2023-8-9, Luyen thi JLPT N4,2023-8-13,2023-9-9,2023-8-11, On thi N3,false "
    })
    public void testAssignment(int userId,int classId, String created_date,String instruction,String due_date,String modified_date,String start_date,String title,boolean isDraft){
             try{
                 Assignment assignment = Assignment.builder()
                         .user(User.builder().id(userId).build())
                         .classroom(Class.builder().id(classId).build())
                         .created_date(dateFormat.parse(created_date))
                         .instruction(instruction)
                         .due_date(dateFormat.parse(due_date))
                         .modified_date(dateFormat.parse(modified_date))
                         .start_date(dateFormat.parse(start_date))
                         .title(title)
                         .is_draft(isDraft)
                         .build();

                 assertThat(assignment).isNotNull();
                 assertThat(assignment.getUser().getId()).isEqualTo(userId);
                 assertThat(assignment.getClassroom().getId()).isEqualTo(classId);
                 assertThat(assignment.getCreated_date()).isEqualTo(created_date);
                 assertThat(assignment.getInstruction()).isEqualTo(instruction);
                 assertThat(assignment.getDue_date()).isEqualTo(due_date);
                 assertThat(assignment.getModified_date()).isEqualTo(modified_date);
                 assertThat(assignment.getStart_date()).isEqualTo(start_date);
                 assertThat(assignment.getTitle()).isEqualTo(title);
                 assertThat(assignment.is_draft()).isEqualTo(isDraft);

             } catch (ParseException e) {
                 throw new RuntimeException(e);
             }
    }
}
