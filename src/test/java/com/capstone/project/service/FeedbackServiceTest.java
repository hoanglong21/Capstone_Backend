package com.capstone.project.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.*;
import com.capstone.project.service.impl.FeedbackServiceImpl;
import com.capstone.project.service.impl.StudySetServiceImpl;
import com.capstone.project.service.impl.UserServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.AssertionErrors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAllFeedbacks() {
        Feedback feedback = Feedback.builder()
                .title("stub")
                .build();

        List<Feedback> feedbacks = List.of(feedback);
        when(feedbackRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(feedbacks);

        assertThat(feedbackService.getAllFeedbacks().size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => userId={0}, title={1}, destination={2}, feedbackTypeId={3}, content={4}")
    @CsvSource({
            "1, title1, destination1, 1, content1",
            "2, title2, destination2, 2, content2",
    })
    public void createFeedback(int userId, String title, String destination, int feedbackTypeId, String content) {
        Feedback feedback = Feedback.builder()
                .user(User.builder().id(userId).build())
                .title(title)
                .destination(destination)
                .feedbackType(FeedbackType.builder().id(feedbackTypeId).build())
                .content(content)
                .build();

        when(feedbackRepository.save(any())).thenReturn(feedback);
        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        assertThat(createdFeedback).isEqualTo(feedback);
    }

    @Order(3)
    @Test
    public void getFeedbackById() {
        Feedback feedback = Feedback.builder().title("stub").build();
        when(feedbackRepository.findById(any())).thenReturn(Optional.ofNullable(feedback));
        try {
            Feedback getFeedback = feedbackService.getFeedbackById(1);
            assertThat(getFeedback).isEqualTo(feedback);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => userId={0}, title={1}, destination={2}, feedbackTypeId={3}, content={4}")
    @CsvSource({
            "1, title1, destination1, 1, content1",
            "2, title2, destination2, 2, content2",
    })
    public void updateFeedback(int userId, String title, String destination, int feedbackTypeId, String content) {
        Feedback feedback = Feedback.builder()
                .title("stub")
                .build();

        Feedback feedbackDetails = Feedback.builder()
                .user(User.builder().id(userId).build())
                .title(title)
                .destination(destination)
                .feedbackType(FeedbackType.builder().id(feedbackTypeId).build())
                .content(content)
                .build();

        when(feedbackRepository.findById(any())).thenReturn(Optional.ofNullable(feedback));
        when(feedbackRepository.save(any())).thenReturn(feedbackDetails);
        try {
            Feedback createdFeedback = feedbackService.updateFeedback(1, feedback);
            assertThat(createdFeedback).isEqualTo(feedbackDetails);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(5)
    @Test
    public void deleteFeedback() {
        Feedback feedback = Feedback.builder()
                .id(1)
                .title("stub")
                .build();
        when(feedbackRepository.findById(any())).thenReturn(Optional.ofNullable(feedback));
        doNothing().when(feedbackRepository).delete(feedback);
        try {
            feedbackService.deleteFeedback(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(feedbackRepository, times(1)).delete(feedback);
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({
            // Add test cases here in the format: "search, type, authorId, authorName, destination, fromCreated, toCreated, sortBy, direction, page, size, expectedTotalItems, expectedTotalPages"
            "'keyword', 1, 0, '', 'destination', '2023-07-01', '2023-07-23', 'created_date', 'asc', 1, 10, true",
    })
    void testFilterFeedback(String search, int type, int authorId, String authorName, String destination,
                            String fromCreated, String toCreated, String sortBy, String direction,
                            int page, int size, boolean greaterThanZero) throws ParseException {
        MockitoAnnotations.openMocks(this);
        try {
            Feedback feedback = Feedback.builder()
                    .user(User.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();

            List<Feedback> resultListMock = new ArrayList<>();
            if(greaterThanZero) {
                TypedQuery<Feedback> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Feedback.class))).thenReturn(typedQueryMock);
                resultListMock.add(feedback);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            } else {
                TypedQuery<Feedback> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Feedback.class))).thenReturn(typedQueryMock);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            }
            List<Feedback> list = (List<Feedback>) feedbackService.filterFeedback(search, type, authorId, authorName, destination,
                    fromCreated, toCreated, sortBy, direction,
                    page, size).get("list");
            assertThat(list.size()>0).isEqualTo(greaterThanZero);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
