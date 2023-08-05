package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.HistoryRepository;
import com.capstone.project.service.impl.HistoryServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;
    
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private HistoryServiceImpl historyService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAll() {
        History history = History.builder()
                .user(User.builder().username("stub").build())
                .build();

        List<History> histories = List.of(history);
        when(historyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(histories);

        assertThat(historyService.getAll().size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "index => typeId={0}, studySetId={1}, classId={2}, userId={3}, datetime={4}")
    @CsvSource({
            "1, 0, 0, 1, 2023-7-1",
            "2, 1, 0, 1, 2023-7-1",
            "3, 0, 1, 1, 2023-7-1"
    })
    public void createHistory(int typeId, int studySetId, int classId, int userId, String datetime) {
        try {
            History history = History.builder()
                    .historyType(HistoryType.builder().id(typeId).build())
                    .user(User.builder().id(userId).build())
                    .studySet(StudySet.builder().id(studySetId).build())
                    .classroom(Class.builder().id(classId).build())
                    .datetime(dateFormat.parse(datetime))
                    .build();


            when(historyRepository.save(any())).thenReturn(history);
            History createdHistory= historyService.createHistory(history);
            assertThat(createdHistory).isEqualTo(history);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @Test
    public void getHistoryById() {
        History history = History.builder().user(User.builder().username("stub").build()).build();
        when(historyRepository.findById(any())).thenReturn(Optional.ofNullable(history));
        try {
            History getHistory = historyService.getHistoryById(1);
            assertThat(getHistory).isEqualTo(history);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => userId={0}, destinationId={1}, typeId={2}, categoryId={3}, fromDatetime={4}, toDatetime={5}," +
            " sortBy={6}, direction={7}, page={8}, pageSize={9}, greaterThanZero={10}")
    @CsvSource({
            "1, 0, 1, 0, 2023-07-01, 2023-07-10, datetime, DESC, 1, 5, true",
            "1, 1, 2, 0, 2023-07-01, 2023-07-10, datetime, DESC, 1, 5, false",
    })
    public void filterUserAchievement(int userId, int destinationId, int typeId, int categoryId, String fromDatetime, String toDatetime,
                              String sortBy, String direction, int page, int size, boolean greaterThanZero) {

        try {
            History history = History.builder()
                    .historyType(HistoryType.builder().id(1).build())
                    .user(User.builder().id(1).build())
                    .datetime(dateFormat.parse("2023-07-06"))
                    .build();

            if(greaterThanZero) {
                ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);
                when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), paramsCaptor.capture()))
                        .thenReturn(1L);
                when(jdbcTemplate.query(anyString(), paramsCaptor.capture(), any(RowMapper.class))).thenReturn(List.of(history));
            } else {
                ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);
                when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), paramsCaptor.capture()))
                        .thenReturn(0L);
                when(jdbcTemplate.query(anyString(), paramsCaptor.capture(), any(RowMapper.class))).thenReturn(List.of());
            }
            List<History> list = (List<History>) historyService.filterHistory(userId, destinationId, typeId, categoryId, fromDatetime, toDatetime,
                    sortBy, direction, page, size).get("list");
            assertThat(list.size()>0).isEqualTo(greaterThanZero);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
