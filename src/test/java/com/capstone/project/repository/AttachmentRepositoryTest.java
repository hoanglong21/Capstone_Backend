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
public class AttachmentRepositoryTest {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getAttachmentBySubmissionId(Boolean trueId){
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Assignment assignment = Assignment.builder().instruction("Assignment for all").title("Assignment 1")
                .classroom(classroom)
                .user(user).build();
        assignmentRepository.save(assignment);

         Submission submission = Submission.builder().description("submit assignment").user(user).assignment(assignment).build();
         submissionRepository.save(submission);

        Attachment attachment = Attachment.builder().attachmentType(AttachmentType.builder().id(1).build())
                .file_url("tailieu.docx")
                .assignment(assignment)
                .submission(submission).build();
        attachmentRepository.save(attachment);
        if(trueId) {
            List<Attachment> result = attachmentRepository.getAttachmentBySubmissionId(submission.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Attachment> result = attachmentRepository.getAttachmentBySubmissionId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }


    @Order(2)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getAttachmentByAssignmentId(Boolean trueId){
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Assignment assignment = Assignment.builder().instruction("Assignment for all").title("Assignment 1")
                .classroom(classroom)
                .user(user).build();
        assignmentRepository.save(assignment);

        Submission submission = Submission.builder().description("submit assignment").user(user).assignment(assignment).build();
        submissionRepository.save(submission);

        Attachment attachment = Attachment.builder().attachmentType(AttachmentType.builder().id(1).build()).file_url("tailieu.docx")
                .assignment(assignment)
                .submission(submission).build();
        attachmentRepository.save(attachment);
        if(trueId) {
            List<Attachment> result = attachmentRepository.getAttachmentByAssignmentId(assignment.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Attachment> result = attachmentRepository.getAttachmentByAssignmentId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void getAttachmentByPostId(Boolean trueId){
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Assignment assignment = Assignment.builder().instruction("Assignment for all").title("Assignment 1").classroom(classroom)
                .user(user).build();
        assignmentRepository.save(assignment);

        Submission submission = Submission.builder().description("submit assignment").user(user).assignment(assignment).build();
        submissionRepository.save(submission);

        Post post = Post.builder().content("Documents for semester").classroom(classroom).user(user).build();
        postRepository.save(post);

        Attachment attachment = Attachment.builder().attachmentType(AttachmentType.builder().id(1).build()).file_url("tailieu.docx")
                .assignment(assignment)
                .post(post)
                .submission(submission).build();
        attachmentRepository.save(attachment);
        if(trueId) {
            List<Attachment> result = attachmentRepository.getAttachmentByPostId(post.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Attachment> result = attachmentRepository.getAttachmentByPostId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

}
