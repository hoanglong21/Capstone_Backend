package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExampleTest {
    @ParameterizedTest(name = "index => exampleText={0}, exampleSentenceJapanese={1}, exampleSentenceVietnamese={2}")
    @CsvSource({
            "ExampleText1, ExampleSentenceJapanese1, ExampleSentenceVietnamese1",
            "ExampleText2, ExampleSentenceJapanese2, ExampleSentenceVietnamese2"
    })
    public void testExample(String exampleText, String exampleSentenceJapanese, String exampleSentenceVietnamese) {
        Example example = Example.builder()
                .exampleText(exampleText)
                .exampleSentenceJapanese(exampleSentenceJapanese)
                .exampleSentenceVietnamese(exampleSentenceVietnamese)
                .build();

        assertThat(example).isNotNull();
        assertThat(example.getExampleText()).isEqualTo(exampleText);
        assertThat(example.getExampleSentenceJapanese()).isEqualTo(exampleSentenceJapanese);
        assertThat(example.getExampleSentenceVietnamese()).isEqualTo(exampleSentenceVietnamese);
    }
}
