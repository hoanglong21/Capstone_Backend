package com.capstone.project.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.CardRepository;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.repository.StudySetRepository;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.impl.StudySetServiceImpl;
import com.capstone.project.service.impl.UserServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudySetServiceTest {
    @Mock
    private StudySetRepository studySetRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardService cardService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StudySetServiceImpl studySetServiceImpl;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void testGetAllStudySets() {
        StudySet studySet = StudySet.builder()
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .build();

        List<StudySet> studySets = List.of(studySet);
        when(studySetRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(studySets);

        assertThat(studySetServiceImpl.getAllStudySets().size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => userId={0}, title={1}, description={2}," +
            " is_deleted={3}, is_public={4}, is_draft={5}, studySetTypeId={6}, deleted_date={7}")
    @CsvSource({
            "1, title1, description1, true, false, false, 1, 2023-6-7",
            "2, title2, description2, false, true, false, 2, 2023-6-8",
    })
    public void testCreateStudySet(int userId, String title, String description, boolean is_deleted, boolean is_public,
                        boolean is_draft, int studySetTypeId, String deleted_date) {
        try {
            StudySet studySet = StudySet.builder()
                    .user(User.builder().id(userId).build())
                    .title(title)
                    .description(description)
                    .is_public(is_public)
                    .is_draft(is_draft)
                    .is_deleted(is_deleted)
                    .studySetType(StudySetType.builder().id(studySetTypeId).build())
                    .deleted_date(dateFormat.parse(deleted_date))
                    .build();
            when(studySetRepository.save(any())).thenReturn(studySet);

            // test
            StudySet createdStudySet = studySetServiceImpl.createStudySet(studySet);
            assertThat(studySet).isEqualTo(createdStudySet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => userId={0}, title={1}, description={2}," +
            " is_deleted={3}, is_public={4}, is_draft={5}, studySetTypeId={6}, deleted_date={7}")
    @CsvSource({
            "1, title1, description1, true, false, true, 1, 2023-6-7",
            "2, title2, description2, false, true, false, 2, 2023-6-8"
    })
    public void testUpdateStudySet(int userId, String title, String description, boolean is_deleted, boolean is_public,
                            boolean is_draft, int studySetTypeId, String deleted_date) {
        try {
            StudySet studySet = StudySet.builder()
                    .user(User.builder().id(10).build())
                    .title("init title")
                    .description("init description")
                    .is_public(false)
                    .is_draft(false)
                    .is_deleted(false)
                    .studySetType(StudySetType.builder().id(10).build())
                    .deleted_date(dateFormat.parse("2023-1-1"))
                    .build();

            StudySet studySetDetails = StudySet.builder()
                    .user(User.builder().id(userId).build())
                    .title(title)
                    .description(description)
                    .is_public(is_public)
                    .is_draft(is_draft)
                    .is_deleted(is_deleted)
                    .studySetType(StudySetType.builder().id(studySetTypeId).build())
                    .deleted_date(dateFormat.parse(deleted_date))
                    .build();
            when(studySetRepository.findById(any())).thenReturn(Optional.ofNullable(studySet));
            when(studySetRepository.save(any())).thenReturn(studySetDetails);

            // test
            StudySet createdStudySet = studySetServiceImpl.updateStudySet(1, studySetDetails);
            assertThat(createdStudySet).isEqualTo(studySetDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @Test
    public void deleteStudySet() {
        StudySet studySet = StudySet.builder()
                .title("Stub")
                .is_deleted(false)
                .studySetType(StudySetType.builder().id(1).build())
                .build();

        when(studySetRepository.findById(any())).thenReturn(Optional.ofNullable(studySet));
        try {
            studySetServiceImpl.deleteStudySet(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        assertThat(studySet.is_deleted()).isEqualTo(true);
    }

    @Order(5)
    @Test
    public void deleteHardStudySet() {
        StudySet studySet = StudySet.builder()
                .id(1)
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .build();

        Card card = Card.builder().id(1).studySet(studySet).build();
        Content content = Content.builder().id(1).card(card).build();

        doNothing().when(studySetRepository).delete(studySet);
        doNothing().when(cardRepository).delete(card);
        doNothing().when(contentRepository).delete(content);

        when(studySetRepository.findById(1)).thenReturn(Optional.of(studySet));
        when(cardRepository.getCardByStudySetId(1)).thenReturn(List.of(card));
        when(contentRepository.getContentByCardId(1)).thenReturn(List.of(content));

        try {
            studySetServiceImpl.deleteHardStudySet(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }

        verify(studySetRepository, times(1)).delete(studySet);
        verify(cardRepository, times(1)).delete(card);
        verify(contentRepository, times(1)).delete(content);
    }

    @Order(6)
    @Test
    public void checkBlankCard() {
        StudySet studySet = StudySet.builder()
                .id(1)
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .build();
        Card card = Card.builder().id(1).studySet(studySet).build();

        when(studySetRepository.findById(anyInt())).thenReturn(Optional.of(studySet));
        when(cardRepository.getCardByStudySetId(anyInt())).thenReturn(List.of(card));
        try {
            when(cardService.checkBlank(anyInt())).thenReturn(true);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }

        try {
            List<Integer> list = studySetServiceImpl.checkBlankCard(1);
            assertThat(list.size()).isGreaterThan(0);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(7)
    @Test
    public void getAllStudySetByUser() {
        User user = User.builder().username("stubUser").build();
        StudySet studySet = StudySet.builder()
                .id(1)
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .build();


        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
        when(studySetRepository.findStudySetByAuthor_id(anyInt())).thenReturn(List.of(studySet));

        try {
            List<StudySet> list = studySetServiceImpl.getAllStudySetByUser("stubUser");
            assertThat(list.size()).isGreaterThan(0);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(8)
    @ParameterizedTest(name = "{index} => isDeleted={0}, isPublic={1}, isDraft={2}, search={3}, type={4}, authorId={5}, authorName={6}, " +
            "fromDeleted={7}, toDeleted={8}, fromCreated={9}, toCreated={10}, sortBy={11}, direction={12}, page={13}, size={14}")
    @CsvSource({
            "true, false, null, , 1, 0, , , , , , id, ASC, 1, 5",
            "false, true, false, test, 2, 0, , , , , , title, DESC, 1, 5"
    })
    public void testGetFilterList(Boolean isDeleted, Boolean isPublic, Boolean isDraft, String search, int type, int authorId, String authorName,
                           String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                           String sortBy, String direction, int page, int size) {
        MockitoAnnotations.openMocks(this);
        StudySet studySet = StudySet.builder()
                .id(1)
                .title("Stub")
                .studySetType(StudySetType.builder().id(1).build())
                .build();
        Query mockedQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString(), eq("StudySetResponseCustomListMapping"))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(List.of(studySet));

        List<StudySet> list = (List<StudySet>) studySetServiceImpl.getFilterList(isDeleted, isPublic, isDraft, search, type, authorId, authorName,
                fromDeleted, toDeleted, fromCreated, toCreated, sortBy, direction, page, size).get("list");

        assertThat(list.size()).isGreaterThan(0);
    }

    @Order(9)
    @Test
    public void getQuizByStudySetId() {
        when(cardService.getAllByStudySetId(anyInt())).thenReturn(new ArrayList<>());
        when(contentRepository.getContentByCardId(anyInt())).thenReturn(new ArrayList<>());


        List<Map<String, Object>> response = null;
        try {
            response = studySetServiceImpl.getQuizByStudySetId(1, new int[]{1}, 1, 1, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertThat(response.size()).isEqualTo(0);

    }


}
