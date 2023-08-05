package com.capstone.project.repository;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
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
public class SubmissionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetSubmissionByAssignmentId(Boolean trueId){

        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Assignment assignment = Assignment.builder().instruction("Assignment for all").title("Assignment 1").classroom(classroom)
                .user(user).build();
        assignmentRepository.save(assignment);

        Submission submission =Submission.builder().user(user).assignment(assignment).description("Submit assignment")
                .build();
        submissionRepository.save(submission);
        if(trueId) {
            List<Submission> result = submissionRepository.getSubmissionByAssignmentId(assignment.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Submission> result = submissionRepository.getSubmissionByAssignmentId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }


}
