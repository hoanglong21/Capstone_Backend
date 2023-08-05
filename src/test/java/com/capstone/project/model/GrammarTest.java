package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GrammarTest {
    @ParameterizedTest(name = "index => title={0}, explanation={1}, structure={2}, attention={3}, about={4}, level={5}, example1={6}, example2={7}")
    @CsvSource({
            "title, explanation, explanation, structure, attention, about, level, example1, example2"
    })
    public void testGrammar(String title, String explanation, String structure, String attention,
                            String about, String level, String example1, String example2) {
        List<String> example = List.of(example1, example2);
        Grammar grammar = Grammar.builder()
                .title(title)
                .explanation(explanation)
                .structure(structure)
                .attention(attention)
                .about(about)
                .level(level)
                .example(example)
                .build();

        assertThat(grammar).isNotNull();
        assertThat(grammar.getTitle()).isEqualTo(title);
        assertThat(grammar.getExplanation()).isEqualTo(explanation);
        assertThat(grammar.getStructure()).isEqualTo(structure);
        assertThat(grammar.getAttention()).isEqualTo(attention);
        assertThat(grammar.getAbout()).isEqualTo(about);
        assertThat(grammar.getLevel()).isEqualTo(level);
        assertThat(grammar.getExample()).isEqualTo(example);
    }
}
