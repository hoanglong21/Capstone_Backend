package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.ClassLearnerRepository;
import com.capstone.project.repository.UserAchievementRepository;
import com.capstone.project.service.impl.ClassLeanerServiceImpl;
import com.capstone.project.service.impl.UserAchievementServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassLearnerServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private ClassLearnerRepository classLearnerRepository;

    @InjectMocks
    private ClassLeanerServiceImpl classLeanerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Order(1)
    @Test
    public void testGetAllClasslearner() {
        try {
            ClassLearner classLearner = ClassLearner.builder()
                    .user(User.builder().id(1).build())
                    .classroom(Class.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();

            List<ClassLearner> classLearners = List.of(classLearner);
            when(classLearnerRepository.findAll()).thenReturn(classLearners);
            assertThat(classLeanerService.getAll().size()).isGreaterThan(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Order(2)
    @Test
    void testGetClassLearnerById() {
        try {

            ClassLearner classLearner = ClassLearner.builder().user(User.builder().id(1).build())
                    .classroom(Class.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();

            when(classLearnerRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(classLearner));
            ClassLearner getClassLearner = classLeanerService.getClassLeanerById(1);
            assertThat(getClassLearner).isEqualTo(classLearner);
        } catch (ParseException | ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @Test
    void testGetClassLearnerByUserId() {
        try {
            List<ClassLearner> classLearners = new ArrayList<>();
            ClassLearner classLearner1 = ClassLearner.builder().user(User.builder().id(1).build()).classroom(Class.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();
            ClassLearner classLearner2 = ClassLearner.builder().user(User.builder().id(2).build()).classroom(Class.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();
            classLearners.add(classLearner1);
            classLearners.add(classLearner2);
            when(classLearnerRepository.getClassLeanerByUserId(any(Integer.class))).thenReturn((ClassLearner) classLearners);
            ClassLearner retrievedClassLearner = classLeanerService.getClassLeanerByUserId(1);
            assertThat(retrievedClassLearner).isEqualTo(classLearners);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(4)
    @ParameterizedTest(name = "index => userId={0}, classId={1}")
    @CsvSource({
            "1, 1 ",
            "2, 1  "
    })
    public void testCreateClassLearner(int userId, int classId) {
        ClassLearner classLearner = ClassLearner.builder()
                .user(User.builder().id(userId).build())
                .classroom(Class.builder().id(classId).build()).build();
        when(classLearnerRepository.save(any())).thenReturn(classLearner);

        ClassLearner createdclasslearner = classLeanerService.createClassLearner(classLearner);
        assertThat(classLearner).isEqualTo(createdclasslearner);
    }

    @Order(5)
    @ParameterizedTest(name = "index => userId{0}, clasId={1}, fromCreated{2},toCreated{3} ," +
            " isAccepted{4}, sortBy{5},direction{6},page={7}, size{8}, greaterThanZero{9} ")
    @CsvSource({
            "1,1,2023-8-9,2023-8-15,created_date,DESC,1,5, true",
            "2,1,2023-8-9,2023-8-15,created_date,DESC,1,5, false"
    })
    public void testFilterClassLearner(int userId, int classId, String fromCreated, String toCreated, Boolean isAccepted,
                                       String sortBy, String direction, int page, int size, boolean greaterThanZero) throws ResourceNotFroundException {

        try {
            MockitoAnnotations.openMocks(this);
            ClassLearner classLearner = ClassLearner.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classId).build()).build();

            List<ClassLearner> resultListMock = new ArrayList<>();
            if (greaterThanZero) {
                TypedQuery<ClassLearner> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(ClassLearner.class))).thenReturn(typedQueryMock);
                resultListMock.add(classLearner);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            } else {
                TypedQuery<ClassLearner> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(ClassLearner.class))).thenReturn(typedQueryMock);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            }
            List<ClassLearner> list = (List<ClassLearner>) classLeanerService.filterClassLearner(userId,classId,fromCreated,toCreated,isAccepted,sortBy,direction,page,size).get("list");
            assertThat(list.size() > 0).isEqualTo(greaterThanZero);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
