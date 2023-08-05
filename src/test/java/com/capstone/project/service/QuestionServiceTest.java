package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.AnswerRepository;
import com.capstone.project.repository.CommentRepository;
import com.capstone.project.repository.QuestionRepository;
import com.capstone.project.service.impl.PostServiceImpl;
import com.capstone.project.service.impl.QuestionServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionServiceTest {

    @Mock
    private EntityManager em;
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionServiceImpl questionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    void testGetAllQuestion() {
        Question question = Question.builder().question("Who kill Jack Robin").build();

        List<Question> questions = List.of(question);
        when(questionRepository.findAll()).thenReturn(questions);
        assertThat(questionServiceImpl.getAllQuestions().size()).isGreaterThan(0);

    }

    @Order(2)
    @Test
    void testGetAllQuestionByTestId() {

        List<Question> questions = new ArrayList<>();
        Question question1 = Question.builder().question("Who kill Jack Robin").build();
        Question question2 = Question.builder().question("Who is the first president").build();
        questions.add(question1);
        questions.add(question2);

        when(questionRepository.getQuestionByTestId(any(Integer.class))).thenReturn(questions);
        List<Question> retrievedQuestions = questionServiceImpl.getAllByTestId(1);
        assertThat(retrievedQuestions).isEqualTo(questions);
    }

    @Order(3)
    @Test
    void testGetQuestionById() {
        Question question = Question.builder()
                .question("Who kill Jack Robin").build();
        when(questionRepository.findById(any())).thenReturn(Optional.ofNullable(question));
        try {
            Question getQuestion = questionServiceImpl.getQuestionById(1);
            assertThat(getQuestion).isEqualTo(question);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }

    }


    @Order(4)
    @ParameterizedTest(name = "index => testId={0}, typeId={1}, num_choice{2}, content{3}")
    @CsvSource({
            "1,1,2, Who kill Jack Robin ",
            "2,2,3, Who is the first president "
    })
    public void testCreateQuestion(int testId, int typeId, int num_choice, String content) {

        Question question = Question.builder()
                .test(com.capstone.project.model.Test.builder().id(testId).build())
                .questionType(QuestionType.builder().id(typeId).build())
                .num_choice(num_choice)
                .question(content)
                .build();
        when(questionRepository.save(any())).thenReturn(question);
        Question createdquestion = questionServiceImpl.createQuestion(question);
        assertThat(question).isEqualTo(createdquestion);
    }

    @Order(5)
    @Test
    void testCreateQuestions() {

        List<Question> questionList = new ArrayList<>();
        Question question1 = Question.builder()
                .test(com.capstone.project.model.Test.builder().id(1).build())
                .questionType(QuestionType.builder().id(1).build())
                .num_choice(2)
                .question("Who kill Tim")
                .build();
        Question question2 = Question.builder()
                .test(com.capstone.project.model.Test.builder().id(1).build())
                .questionType(QuestionType.builder().id(1).build())
                .num_choice(2)
                .question("Who kill Jack")
                .build();
        questionList.add(question1);
        questionList.add(question2);

        when(questionRepository.save(any())).thenReturn(questionList);

        List<Question> createdQuestions = questionServiceImpl.createQuestions(questionList);

        assertThat(createdQuestions).isNotNull();
    }


    @Order(6)
    @ParameterizedTest(name = "index => testId={0}, typeId={1}, num_choice{2}, content{3}")
    @CsvSource({
            "1,1,2, Who kill Jack Robin ",
            "2,2,3, Who is the first president "
    })
    public void testUpdateQuestion(int testId, int typeId, int num_choice, String content) {

        Question question_new = Question.builder()
                .num_choice(3)
                .question("What time is it")
                .build();

        Question question = Question.builder()
                .test(com.capstone.project.model.Test.builder().id(testId).build())
                .questionType(QuestionType.builder().id(typeId).build())
                .num_choice(num_choice)
                .question(content)
                .build();
        when(questionRepository.findById(any())).thenReturn(Optional.ofNullable(question_new));
        when(questionRepository.save(any())).thenReturn(question);

    }

    @Order(7)
    @Test
    void testDeleteQuestion() {

        Question question = Question.builder()
                .id(1)
                .test(com.capstone.project.model.Test.builder().id(1).build())
                .questionType(QuestionType.builder().id(1).build())
                .num_choice(4)
                .question("Who kill Jack Robin")
                .build();
        Answer answer = Answer.builder()
                .id(1)
                .question(Question.builder().id(1).build())
                .content("Mango")
                .is_true(false)
                .build();
        doNothing().when(questionRepository).delete(question);
        doNothing().when(answerRepository).delete(answer);

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        when(answerRepository.getAnswerByQuestionId(1)).thenReturn(List.of(answer));
        try {
            questionServiceImpl.deleteQuestion(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(questionRepository, times(1)).delete(question);
        verify(answerRepository, times(1)).delete(answer);

    }

    @Order(8)
    @ParameterizedTest(name = "index => search={0},typeid{1},testid{2}, page{3}, size{4}")
    @CsvSource({
            "who,1,1,1,5",
            "who,1,2,1,5"
    })
    public void testGetFilterQuestion(String search, int typeid, int testid, int page, int size) throws ResourceNotFroundException {

        MockitoAnnotations.openMocks(this);
        Question question = Question.builder()
                .id(1)
                .test(com.capstone.project.model.Test.builder().id(1).build())
                .questionType(QuestionType.builder().id(1).build())
                .num_choice(4)
                .question("Who kill Jack Robin")
                .build();

        Query mockedQuery = mock(Query.class);
        when(em.createNativeQuery(anyString(), eq(Question.class))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(question));

        List<Question> list = (List<Question>) questionServiceImpl.getFilterQuestion(search, typeid, testid, page, size).get("list");
        assertThat(list.size()).isGreaterThan(0);
    }

}
