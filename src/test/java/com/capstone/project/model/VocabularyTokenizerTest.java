package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VocabularyTokenizerTest {
    @ParameterizedTest(name = "index => word={0}, partOfSpeech1={1}, partOfSpeech2={2}, dictionaryForm={3}")
    @CsvSource({
            "word1, partOfSpeech1, partOfSpeech2, dictionaryForm1",
            "word2, partOfSpeech3, partOfSpeech4, dictionaryForm2"
    })
    public void testVocabularyTokenizer(String word, String partOfSpeech1, String partOfSpeech2, String dictionaryForm){
        List<String> partOfSpeech = List.of(partOfSpeech1, partOfSpeech2);

        VocabularyTokenizer vocabularyTokenizer = VocabularyTokenizer.builder()
                .word(word)
                .partOfSpeech(partOfSpeech)
                .dictionaryForm(dictionaryForm)
                .build();

        assertThat(vocabularyTokenizer).isNotNull();
        assertThat(vocabularyTokenizer.getWord()).isEqualTo(word);
        assertThat(vocabularyTokenizer.getPartOfSpeech()).isEqualTo(partOfSpeech);
        assertThat(vocabularyTokenizer.getDictionaryForm()).isEqualTo(dictionaryForm);
    }
}
