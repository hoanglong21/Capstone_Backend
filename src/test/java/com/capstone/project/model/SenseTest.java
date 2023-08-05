package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SenseTest {
    @ParameterizedTest(name = "index => type1={0}, type2{1}, relate1={2}, relate2={3}, definition1={4}, definition2={5}," +
            " exampleText1={6}, exampleSentenceJapanese1={7}, exampleSentenceVietnamese1={8}," +
            " exampleText2={9}, exampleSentenceJapanese2={10}, exampleSentenceVietnamese2={11}")
    @CsvSource({
            "type1, type2, relate1, relate2, definition1, definition2, " +
            "ExampleText1, ExampleSentenceJapanese1, ExampleSentenceVietnamese1, " +
            "ExampleText2, ExampleSentenceJapanese2, ExampleSentenceVietnamese2"
    })
    public void testExample(String type1, String type2, String relate1, String relate2, String definition1, String definition2,
                            String exampleText1, String exampleSentenceJapanese1, String exampleSentenceVietnamese1,
                            String exampleText2, String exampleSentenceJapanese2, String exampleSentenceVietnamese2) {
        List<String> type = List.of(type1, type2);
        List<String> relate = List.of(relate1, relate2);
        List<String> definition = List.of(definition1, definition2);
        List<Example> example = List.of(new Example(exampleText1, exampleSentenceJapanese1, exampleSentenceVietnamese1)
                , new Example(exampleText2, exampleSentenceJapanese2, exampleSentenceVietnamese2));
        Sense sense = Sense.builder()
                .type(type)
                .relate(relate)
                .definition(definition)
                .example(example)
                .build();

        assertThat(sense).isNotNull();
        assertThat(sense.getType()).isEqualTo(type);
        assertThat(sense.getRelate()).isEqualTo(relate);
        assertThat(sense.getDefinition()).isEqualTo(definition);
        assertThat((sense.getExample())).isEqualTo(example);
    }
}
