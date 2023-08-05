package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.model.Content;
import com.capstone.project.model.Field;
import com.capstone.project.model.StudySet;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.service.impl.ContentServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContentServiceTest {
    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private ContentServiceImpl contentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    public void getAllByCardId() {
        Content content = Content.builder().id(1).build();

        List<Content> contentList = List.of(content);
        when(contentRepository.getContentByCardId(anyInt())).thenReturn(contentList);

        assertThat(contentService.getAllByCardId(1).size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => cardId={0}, fieldId={1}, content={2}")
    @CsvSource({
            "1, 1, content1",
            "2, 2, content2",
    })
    public void createContent(int cardId, int fieldId, String contentValue) {
        try {
            Content content = Content.builder()
                    .card(Card.builder().id(cardId).build())
                    .field(Field.builder().id(fieldId).build())
                    .content(contentValue)
                    .build();
            when(contentRepository.save(any())).thenReturn(content);

            // test
            Content createdContent = contentService.createContent(content);
            assertThat(content).isEqualTo(createdContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => cardId={0}, fieldId={1}, content={2}")
    @CsvSource({
            "1, 1, content1",
            "2, 2, content2",
    })
    public void getContentById(int cardId, int fieldId, String contentValue) {
        try {
            Content content = Content.builder()
                    .card(Card.builder().id(cardId).build())
                    .field(Field.builder().id(fieldId).build())
                    .content(contentValue)
                    .build();
            when(contentRepository.findById(any())).thenReturn(Optional.ofNullable(content));

            // test
            Content createdContent = contentService.getContentById(1);
            assertThat(content).isEqualTo(createdContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => cardId={0}, fieldId={1}, content={2}")
    @CsvSource({
            "1, 1, content1",
            "2, 2, content2",
    })
    public void updateContent(int cardId, int fieldId, String contentValue) {
        try {
            Content content = Content.builder()
                    .card(Card.builder().id(1).build())
                    .field(Field.builder().id(1).build())
                    .content("stubContent")
                    .build();

            Content contentDetails = Content.builder()
                    .card(Card.builder().id(cardId).build())
                    .field(Field.builder().id(fieldId).build())
                    .content(contentValue)
                    .build();

            when(contentRepository.findById(anyInt())).thenReturn(Optional.ofNullable(content));
            when(contentRepository.save(any())).thenReturn(contentDetails);

            // test
            Content createdContent = contentService.updateContent(1, contentDetails);
            assertThat(createdContent).isEqualTo(contentDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Order(5)
    @Test
    public void deleteContent() {
        Content content = Content.builder()
                .card(Card.builder().id(1).build())
                .field(Field.builder().id(1).build())
                .content("stubContent")
                .build();

        when(contentRepository.findById(anyInt())).thenReturn(Optional.ofNullable(content));

        doNothing().when(contentRepository).delete(content);

        try {
            contentService.deleteContent(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }

        verify(contentRepository, times(1)).delete(content);
    }
}
