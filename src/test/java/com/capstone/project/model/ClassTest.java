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
public class ClassTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0}, class_name={1}, classcode{2},created_date{3},deleted_date{4}, description{5},is_deleted{6}")
    @CsvSource({
            "1, Luyen thi JLPT N5,gjhktg,2023-7-1,2023-8-7, On thi N3,false ",
            "2, Luyen thi JLPT N4,Jnf5A,2023-8-9,2023-8-7, On thi N3,true "
    })
    public void testClass(int userId, String class_name,String classcode,String created_date,
                          String deleted_date, String description,Boolean is_deleted) {
        try {
            Class classroom = Class.builder()
                    .user(User.builder().id(userId).build())
                    .class_name(class_name)
                    .classcode(classcode)
                    .created_date(dateFormat.parse(created_date))
                    .deleted_date(dateFormat.parse(deleted_date))
                    .description(description)
                    .is_deleted(is_deleted)
                    .build();

            assertThat(classroom).isNotNull();
            assertThat(classroom.getUser().getId()).isEqualTo(userId);
            assertThat(classroom.getClass_name()).isEqualTo(class_name);
            assertThat(classroom.getClasscode()).isEqualTo(classcode);
            assertThat(classroom.getCreated_date()).isEqualTo(created_date);
            assertThat(classroom.getDeleted_date()).isEqualTo(deleted_date);
            assertThat(classroom.getDescription()).isEqualTo(description);
            assertThat(classroom.is_deleted()).isEqualTo(is_deleted);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

    }
}
