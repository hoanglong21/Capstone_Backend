package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.AttachmentRepository;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.repository.PostRepository;
import com.capstone.project.repository.SubmissionRepository;
import com.capstone.project.service.impl.PostServiceImpl;
import com.capstone.project.service.impl.SubmissionServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubmissionServiceTest {

    @Mock
    private EntityManager em;
    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private SubmissionServiceImpl submissionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Order(1)
    @Test
    void testGetAllSubmission() {
        Submission submission = Submission.builder().description("Submit assignment").build();

        List<Submission> submissions = List.of(submission);
        when(submissionRepository.findAll()).thenReturn(submissions);
        assertThat(submissionServiceImpl.getAllSubmission().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testGetSubmissionById() {
        Submission submission = Submission.builder().description("Submit assignment").build();

        when(submissionRepository.findById(any())).thenReturn(Optional.ofNullable(submission));
        try {
            Submission getSubmision = submissionServiceImpl.getSubmissionById(1);
            assertThat(getSubmision).isEqualTo(submission);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @Test
    void testGetAllSubmissionByAssignmentId() {
        List<Submission> submissions = new ArrayList<>();
        Submission submission1 = Submission.builder().description("Submit assignment1").build();
        Submission submission2 = Submission.builder().description("Submit assignment2").build();
        submissions.add(submission1);
        submissions.add(submission2);

        when(submissionRepository.getSubmissionByAssignmentId(any(Integer.class))).thenReturn(submissions);
        List<Submission> retrievedSubmissions = submissionServiceImpl.getAllSubmissionByAssignmentId(1);
        assertThat(retrievedSubmissions).isEqualTo(submissions);
    }

    @Order(4)
    @ParameterizedTest(name = "index => userId={0}, assignmentId={1}, created_date{2}, description{3},modified_date{4}")
    @CsvSource({
            "1,1,2023-4-5,Submit assignment,2023-5-4 ",
            "2,2,2023-4-5, Submit test, 2023-5-4 "
    })
    public void testCreateSubmission(int userId, int assignmetnId, String created_date, String description, String modified_date) {
        try {

            Submission submission = Submission.builder()
                    .user(User.builder().id(userId).build())
                    .assignment(Assignment.builder().id(assignmetnId).build())
                    .created_date(dateFormat.parse(created_date))
                    .description(description)
                    .modified_date(dateFormat.parse(modified_date))
                    .build();

            when(submissionRepository.save(any())).thenReturn(submission);
            Submission createdsubmission = submissionServiceImpl.createSubmission(submission);
            assertThat(submission).isEqualTo(createdsubmission);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(5)
    @ParameterizedTest(name = "index => userId={0}, assignmentId={1}, created_date{2}, description{3},modified_date{4}")
    @CsvSource({
            "1,1,2023-4-5,Submit assignment,2023-5-4 ",
            "2,2,2023-4-5, Submit test, 2023-5-4 "
    })
    public void testUpdateSubmission(int userId, int assignmetnId, String created_date, String description, String modified_date) {
        try {
            Submission submission_new = Submission.builder()
                    .description("Submit midterm test")
                    .build();
            Submission submission = Submission.builder()
                    .user(User.builder().id(userId).build())
                    .assignment(Assignment.builder().id(assignmetnId).build())
                    .created_date(dateFormat.parse(created_date))
                    .description(description)
                    .modified_date(dateFormat.parse(modified_date))
                    .build();

            when(submissionRepository.findById(any())).thenReturn(Optional.ofNullable(submission_new));
            when(submissionRepository.save(any())).thenReturn(submission);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Order(6)
    @Test
    void testDeleteSubmision() {
        Submission submission = Submission.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .assignment(Assignment.builder().id(1).build())
                .description("Submit for assignment")
                .build();
        Attachment attachment = Attachment.builder()
                .assignment(Assignment.builder().id(1).build())
                .attachmentType(AttachmentType.builder().id(1).build())
                .submission(Submission.builder().id(1).build())
                .file_url("home.doc")
                .build();
        doNothing().when(submissionRepository).delete(submission);
        doNothing().when(attachmentRepository).delete(attachment);

        when(submissionRepository.findById(1)).thenReturn(Optional.of(submission));
        when(attachmentRepository.getAttachmentBySubmissionId(1)).thenReturn(List.of(attachment));
        try {
            submissionServiceImpl.deleteSubmission(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(submissionRepository, times(1)).delete(submission);
        verify(attachmentRepository, times(1)).delete(attachment);
    }

    @Order(7)
    @ParameterizedTest(name = "index => search={0},authorId{1}, assignmentId{2},mark{3}, fromCreated{4},toCreated{5}, " +
                                        "direction{6}, page{7}, size{8}")
    @CsvSource({
            "Homework1,1,1,8.2,2023-8-9,2023-8-15,DESC,1,5",
            "Homwork2,2,1,5.5,2023-8-9,2023-8-15,DESC,1,5"
    })
    public void testGetFilterSubmission(String search, int authorId, int assignmentId, double mark, String fromCreated,
                                        String toCreated, String direction, int page, int size) throws ResourceNotFroundException {

        MockitoAnnotations.openMocks(this);
        Submission submission = Submission.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .assignment(Assignment.builder().id(1).build())
                .description("Submit for assignment")
                .build();
        Query mockedQuery = mock(Query.class);
        when(em.createNativeQuery(anyString(), eq(Submission.class))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(submission));

        List<Submission> list = (List<Submission>) submissionServiceImpl.getFilterSubmission(search, authorId, assignmentId,
                                                     mark, fromCreated, toCreated, direction, page, size).get("list");
        assertThat(list.size()).isGreaterThan(0);
    }

}
