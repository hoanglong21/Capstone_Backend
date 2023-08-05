package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.CardRepository;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.service.impl.CardServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAllCards() {
        Card card = Card.builder()
                .studySet(StudySet.builder().id(1).build())
                .build();

        List<Card> cardList = List.of(card);
        when(cardRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(cardList);

        assertThat(cardService.getAllCards().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void getAllByStudySetId() {
        Card card = Card.builder()
                .studySet(StudySet.builder().id(1).build())
                .build();

        List<Card> cardList = List.of(card);
        when(cardRepository.getCardByStudySetId(anyInt())).thenReturn(cardList);

        assertThat(cardService.getAllByStudySetId(1).size()).isGreaterThan(0);
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => studySetId={0}, picture={1}, audio={2}")
    @CsvSource({
            "1, picture1, audio1",
            "2, picture2, audio2",
    })
    public void createCard(int studySetId, String picture, String audio) {
        try {
            Card card = Card.builder()
                    .studySet(StudySet.builder().id(studySetId).build())
                    .picture(picture)
                    .audio(audio)
                    .build();
            when(cardRepository.save(any())).thenReturn(card);

            // test
            Card createdCard = cardService.createCard(card);
            assertThat(card).isEqualTo(createdCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => studySetId={0}, picture={1}, audio={2}")
    @CsvSource({
            "1, picture1, audio1",
            "2, picture2, audio2",
    })
    public void getCardById(int studySetId, String picture, String audio) {
        try {
            Card card = Card.builder()
                    .studySet(StudySet.builder().id(studySetId).build())
                    .picture(picture)
                    .audio(audio)
                    .build();
            when(cardRepository.findById(any())).thenReturn(Optional.ofNullable(card));

            // test
            Card createdCard = cardService.getCardById(1);
            assertThat(card).isEqualTo(createdCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(5)
    @ParameterizedTest(name = "{index} => studySetId={0}, picture={1}, audio={2}")
    @CsvSource({
            "1, picture1, audio1",
            "2, picture2, audio2",
    })
    public void updateCard(int studySetId, String picture, String audio) {
        try {
            Card card = Card.builder()
                    .studySet(StudySet.builder().id(1).build())
                    .picture("stubPicture")
                    .audio("stubAudio")
                    .build();

            Card cardDetails = Card.builder()
                    .studySet(StudySet.builder().id(studySetId).build())
                    .picture(picture)
                    .audio(audio)
                    .build();

            when(cardRepository.findById(any())).thenReturn(Optional.ofNullable(card));
            when(cardRepository.save(any())).thenReturn(cardDetails);

            // test
            Card createdCard = cardService.updateCard(1, cardDetails);
            assertThat(createdCard).isEqualTo(cardDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(6)
    @Test
    public void deleteCard() {
        Card card = Card.builder()
                .studySet(StudySet.builder().id(1).build())
                .picture("stubPicture")
                .audio("stubAudio")
                .build();

        Content content = Content.builder().id(1).card(card).build();

        when(cardRepository.findById(anyInt())).thenReturn(Optional.ofNullable(card));
        when(contentRepository.getContentByCardId(anyInt())).thenReturn(List.of(content));

        doNothing().when(cardRepository).delete(card);
        doNothing().when(contentRepository).delete(content);

        try {
            cardService.deleteCard(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }

        verify(cardRepository, times(1)).delete(card);
        verify(contentRepository, times(1)).delete(content);
    }

    @Order(7)
    @Test
    public void checkBlank() {
        Card card = Card.builder()
                .studySet(StudySet.builder().id(1).build())
                .picture("")
                .audio("")
                .build();

        Content content = Content.builder().id(1).card(card).build();

        when(cardRepository.findById(anyInt())).thenReturn(Optional.of(card));
        when(contentRepository.getContentByCardId(anyInt())).thenReturn(List.of(content));

        try {
            Boolean checkBlank = cardService.checkBlank(1);
            assertThat(checkBlank).isTrue();
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }
}
