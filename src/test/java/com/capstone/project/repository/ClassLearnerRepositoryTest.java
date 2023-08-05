package com.capstone.project.repository;

import com.capstone.project.model.Class;
import com.capstone.project.model.ClassLearner;
import com.capstone.project.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassLearnerRepositoryTest {
    @Autowired
    private ClassLearnerRepository classLearnerRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Order(1)
    @ParameterizedTest(name = "{index} => userId={0}, classId{1}, createdDate{2}" )
    @CsvSource({
            "2, 9, 2023-07-15",
            "2,8, 2023-08-09",
    })
    public void testFindByUserIdAndClassroomId(int userId,int classroomId,String createdDate)  {

        try {
            ClassLearner classLearner = ClassLearner.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classroomId).build())
                    .created_date(dateFormat.parse(createdDate))
                    .build();
            classLearnerRepository.save(classLearner);

            ClassLearner classLearners = classLearnerRepository.findByUserIdAndClassroomId(2,7);
            assertThat(classLearners).isNotNull();
        }catch (ParseException e){
            throw  new RuntimeException(e);
        }
    }

    @Order(2)
    @ParameterizedTest(name = "index => userId={0}, classId={1}, createdDate={2}")
    @CsvSource({
            "2, 9, 2023-7-1",
            "2, 8, 2023-7-2",
    })
    public void testGetClassLeanerByUserId(int userId, int classId, String createdDate) {
        try {
            ClassLearner classLearner = ClassLearner.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classId).build())
                    .created_date(dateFormat.parse(createdDate))
                    .build();

            classLearnerRepository.save(classLearner);

            ClassLearner classLearners = classLearnerRepository.getClassLeanerByUserId(2);
            assertThat(classLearners).isNotNull();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
