package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.UserAchievementRepository;
import com.capstone.project.service.impl.UserAchievementServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
public class UserAchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserAchievementServiceImpl userAchievementService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getUserAchievements() {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(User.builder().id(1).build())
                    .achievement(Achievement.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();

            List<UserAchievement> userAchievements = List.of(userAchievement);
            when(userAchievementRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(userAchievements);

            assertThat(userAchievementService.getAll().size()).isGreaterThan(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Order(2)
    @Test
    public void getAchievementTypeById() {
        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(User.builder().id(1).build())
                    .achievement(Achievement.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();
            when(userAchievementRepository.findById(any())).thenReturn(Optional.ofNullable(userAchievement));

            UserAchievement getUserAchievement = userAchievementService.getUserAchievementById(1);
            assertThat(getUserAchievement).isEqualTo(userAchievement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => userId={0}, achievementId={1}, fromDatetime={2}, toDatetime={3}," +
            " sortBy={4}, direction={5}, page={6}, pageSize={7}, greaterThanZero={8}")
    @CsvSource({
            "1, 1, 2023-07-01, 2023-07-10, datetime, DESC, 1, 5, true",
            "1, 2, 2023-07-01, 2023-07-10, datetime, DESC, 1, 5, false",
    })
    public void filterHistory(int userId, int achievementId, String fromDatetime, String toDatetime,
                              String sortBy, String direction, int page, int size, boolean greaterThanZero) {

        try {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(User.builder().id(1).build())
                    .achievement(Achievement.builder().id(1).build())
                    .created_date(dateFormat.parse("2023-07-06"))
                    .build();

            List<UserAchievement> resultListMock = new ArrayList<>();
            if(greaterThanZero) {
                TypedQuery<UserAchievement> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(UserAchievement.class))).thenReturn(typedQueryMock);
                resultListMock.add(userAchievement);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            } else {
                TypedQuery<UserAchievement> typedQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(UserAchievement.class))).thenReturn(typedQueryMock);
                when(typedQueryMock.getResultList()).thenReturn(resultListMock);

                TypedQuery<Long> countQueryMock = mock(TypedQuery.class);
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQueryMock);
            }
            List<UserAchievement> list = (List<UserAchievement>) userAchievementService.filterUserAchievement(userId, achievementId, fromDatetime, toDatetime,
                    sortBy, direction, page, size).get("list");
            assertThat(list.size()>0).isEqualTo(greaterThanZero);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
