package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VocabularyTest {
    @ParameterizedTest(name = "index => kanji1={0}, kanji2={1}, reading1={2}, reading2={3}, type={4}, relate={5}, " +
            "definition={6}, exampleText={7}, exampleSentenceJapanese={8}, exampleSentenceVietnamese={9},")
    @CsvSource({
            "Kanji1, Kanji2, reading1, reading2, type, relate, definition, exampleText, exampleJapanese, exampleVietnamese"
    })
    public void testVocabulary(String kanji1, String kanji2, String reading1, String reading2, String type,
                               String relate, String definition, String example, String exampleJapanese, String exampleVietnamese) {
        List<String> kanji = List.of(kanji1, kanji2);
        List<String> reading = List.of(reading1, reading2);
        Example exampleCore = new Example(example, exampleJapanese, exampleVietnamese);
        Sense senseCore = Sense.builder()
                .type(List.of(type))
                .relate(List.of(relate))
                .definition(List.of(definition))
                .example(List.of(exampleCore))
                .build();
        List<Sense> sense = List.of(senseCore);
        Vocabulary vocabulary = Vocabulary.builder()
                .kanji(kanji)
                .reading(reading)
                .sense(sense)
                .build();

        assertThat(vocabulary).isNotNull();
        assertThat(vocabulary.getKanji()).isEqualTo(kanji);
        assertThat(vocabulary.getReading()).isEqualTo(reading);
        assertThat(vocabulary.getSense()).isEqualTo(sense);
    }
}
