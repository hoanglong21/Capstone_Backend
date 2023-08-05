package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContentTest {
    @ParameterizedTest(name = "index => cardId={0}, fieldId={1}, content={2}")
    @CsvSource({
            "1, 1, Content1",
            "2, 2, Content2"
    })
    public void testContent(int cardId, int fieldId, String contentValue) {
        Content content = Content.builder()
                .card(Card.builder().id(cardId).build())
                .field(Field.builder().id(fieldId).build())
                .content(contentValue)
                .build();

        assertThat(content).isNotNull();
        assertThat(content.getCard().getId()).isEqualTo(cardId);
        assertThat(content.getField().getId()).isEqualTo(fieldId);
        assertThat(content.getContent()).isEqualTo(contentValue);
    }
}
