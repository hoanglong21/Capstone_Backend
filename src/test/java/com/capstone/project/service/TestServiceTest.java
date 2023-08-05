package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.*;
import com.capstone.project.service.impl.PostServiceImpl;
import com.capstone.project.service.impl.TestServiceImpl;
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
public class TestServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private TestRepository testRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TestServiceImpl testServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Order(1)
    @Test
    void testGetAllTest() {
        com.capstone.project.model.Test testclass = com.capstone.project.model.Test.builder()
                .description("Test week 1")
                .title("PT1").build();

        List<com.capstone.project.model.Test> tests = List.of(testclass);
        when(testRepository.findAll()).thenReturn(tests);
        assertThat(testServiceImpl.getAllTest().size()).isGreaterThan(0);

    }

    @Order(2)
    @Test
    void testGetTestById() {
        com.capstone.project.model.Test testclass = com.capstone.project.model.Test.builder()
                .description("Test week 1")
                .title("PT1").build();
        when(testRepository.findById(any())).thenReturn(Optional.ofNullable(testclass));
        try {
            com.capstone.project.model.Test getTest = testServiceImpl.getTestById(1);
            assertThat(getTest).isEqualTo(testclass);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => username={0}")
    @CsvSource({
            "johnsmith",
            "janedoe"
    })
    void testGetTestByUser(String username) {
        User user = User.builder()
                .username(username)
                .email("johnsmith@example.com")
                .build();
        List<com.capstone.project.model.Test> testList = new ArrayList<>();
        com.capstone.project.model.Test test1 = com.capstone.project.model.Test.builder()
                .description("Test week 1")
                .title("PT1").build();
        com.capstone.project.model.Test test2 = com.capstone.project.model.Test.builder()
                .description("Test week 2")
                .title("PT2").build();
        testList.add(test1);
        testList.add(test2);
        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(testRepository.getTestByAuthorId(user.getId())).thenReturn(testList);
        try {
            List<com.capstone.project.model.Test> result = testServiceImpl.getTestByUser(username);
            assertThat(result).containsExactlyInAnyOrderElementsOf(testList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @ParameterizedTest(name = "index => userId={0}, created_date{1}, description{2},duration{3},modified_date{4},title{5}")
    @CsvSource({
            "1,2023-4-5,Test knowledge,12,2023-5-4, Test daily ",
            "2,2023-4-5, Test all learner,14, 2023-5-4, Midterm "
    })
    public void testCreateTest(int userId, String created_date, String description, int duration, String modified_date,
                               String title) {
        try {
            com.capstone.project.model.Test test = com.capstone.project.model.Test.builder()
                    .user(User.builder().id(userId).build())
                    .created_date(dateFormat.parse(created_date))
                    .description(description)
                    .duration(duration)
                    .modified_date(dateFormat.parse(modified_date))
                    .title(title).build();
            when(testRepository.save(any())).thenReturn(test);
            com.capstone.project.model.Test createdtest = testServiceImpl.createTest(test);
            assertThat(test).isEqualTo(createdtest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Order(5)
    @ParameterizedTest(name = "index => userId={0}, created_date{1}, description{2},duration{3},modified_date{4},title{5}")
    @CsvSource({
            "1,2023-4-5,Test knowledge,12,2023-5-4, Test daily ",
            "2,2023-4-5, Test all learner,14, 2023-5-4, Midterm "
    })
    public void testUpdateTest(int userId, String created_date, String description, int duration, String modified_date,
                               String title) {
        try {

            com.capstone.project.model.Test test_new = com.capstone.project.model.Test.builder()
                    .user(User.builder().id(userId).build())
                    .created_date(dateFormat.parse(created_date))
                    .description(description)
                    .duration(duration)
                    .modified_date(dateFormat.parse(modified_date))
                    .title(title).build();
            com.capstone.project.model.Test test = com.capstone.project.model.Test.builder()
                    .user(User.builder().id(userId).build())
                    .created_date(dateFormat.parse(created_date))
                    .description(description)
                    .duration(duration)
                    .modified_date(dateFormat.parse(modified_date))
                    .title(title).build();
            when(testRepository.findById(any())).thenReturn(Optional.ofNullable(test_new));
            when(testRepository.save(any())).thenReturn(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(6)
    @Test
    void testDeleteTest() {
        com.capstone.project.model.Test test = com.capstone.project.model.Test.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .description("test 1 for all")
                .duration(12)
                .title("PT1")
                .build();
        Question question = Question.builder()
                .id(1)
                .test(com.capstone.project.model.Test.builder().id(1).build())
                .questionType(QuestionType.builder().id(1).build())
                .num_choice(4)
                .question("Who kill Jack Robin")
                .build();
        Answer answer = Answer.builder().question(Question.builder().id(1).build()).content("Mango").is_true(false).build();

        Comment comment = Comment.builder().commentType(CommentType.builder().id(1).build()).content("Forcus").build();
        doNothing().when(testRepository).delete(test);
        doNothing().when(questionRepository).delete(question);
        doNothing().when(answerRepository).delete(answer);
        doNothing().when(commentRepository).delete(comment);

        when(testRepository.findById(1)).thenReturn(Optional.of(test));
        when(questionRepository.getQuestionByTestId(1)).thenReturn(List.of(question));
        when(answerRepository.getAnswerByQuestionId(1)).thenReturn(List.of(answer));
        when(commentRepository.getCommentByTestId(1)).thenReturn(List.of(comment));
        try {
            testServiceImpl.deleteTest(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(testRepository, times(1)).delete(test);
        verify(questionRepository, times(1)).delete(question);
        verify(answerRepository, times(1)).delete(answer);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Order(7)
    @ParameterizedTest(name = "index => search={0},author{1},direction{2}, duration{3},classId={4}, fromStart{5}, " +
                                      "toStart{6},fromCreated{7},toCreated{8} ,isDraft{9}, sortBy{10}, page{11}, size{12}")
    @CsvSource({
            "Homework,quantruong,DESC,45,1,2023-8-9,2023-8-15,2023-8-1,2023-8-5,true,created_date,1,1,5",
            "Homwork,ngocnguyen,DESC,15,2,2023-8-9,2023-8-15,2023-8-1,2023-8-5,false,created_date,1,1,5"
    })
    public void testGetFilterTest(String search, String author, String direction, int duration, int classid,
                                  String fromStarted, String toStarted, String fromCreated, String toCreated, Boolean isDraft,
                                  String sortBy, int page, int size) throws ResourceNotFroundException {

        MockitoAnnotations.openMocks(this);
        com.capstone.project.model.Test test = com.capstone.project.model.Test.builder()
                .id(1)
                .user(User.builder().id(1).build())
                .description("test 1 for all")
                .duration(12)
                .title("PT1")
                .build();

        Query mockedQuery = mock(Query.class);
        when(em.createNativeQuery(anyString(), eq("TestCustomListMapping"))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(test));

        List<com.capstone.project.model.Test> list = (List<com.capstone.project.model.Test>) testServiceImpl.getFilterTest(search, author, direction, duration,
                classid, fromStarted, toStarted, fromCreated, toCreated,
                isDraft, sortBy, page, size).get("list");
        assertThat(list.size()).isGreaterThan(0);
    }

}
