package com.capstone.project.repository;
import com.capstone.project.model.Answer;
import com.capstone.project.model.Assignment;
import com.capstone.project.model.Class;
import com.capstone.project.model.User;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssignmentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getAssignmentByClassroomId(Boolean trueId){
        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Assignment assignment = Assignment.builder()
                .instruction("Assignment for all")
                .title("Assignment 1")
                .classroom(classroom)
                .user(user).build();
        assignmentRepository.save(assignment);

        if(trueId) {
            List<Assignment> result = assignmentRepository.getAssignmentByClassroomId(classroom.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Assignment> result = assignmentRepository.getAssignmentByClassroomId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
