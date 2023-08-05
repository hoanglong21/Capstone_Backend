package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KanjiTest {
    @ParameterizedTest(name = "index => character={0}, gradeLevel={1}, strokeCount={2}, jlptLevel={3}, radical1={4}, radical2={5}, " +
            "readingVietnam1={6}, readingVietnam2={7}, readingJapaneseOn1={8}, readingJapaneseOn2={9}, readingJapaneseKun1, readingJapaneseKun2," +
            "meaning1, meaning2, svgFile")
    @CsvSource({
            "character, 1, 3, 4, radical1, radical2, readingVietnam1, readingVietnam2, readingJapaneseOn1, readingJapaneseOn1, " +
                    "readingJapaneseKun1, readingJapaneseKun2, meaning1, meaning2, svgFile"
    })
    public void testKanji(String character, String gradeLevel, String strokeCount, String jlptLevel, String radical1,
                          String radical2, String readingVietnam1, String readingVietnam2, String readingJapaneseOn1,
                          String readingJapaneseOn2,  String readingJapaneseKun1, String readingJapaneseKun2,
                          String meaning1, String meaning2, String svgFile) {
        List<String> radicals = List.of(radical1, radical2);
        List<String> readingVietnam = List.of(readingVietnam1, readingVietnam2);
        List<String> readingJapaneseOn = List.of(readingJapaneseOn1, readingJapaneseOn2);
        List<String> readingJapaneseKun = List.of(readingJapaneseKun1, readingJapaneseKun2);
        List<String> meaning = List.of(meaning1, meaning2);
        Kanji kanji = Kanji.builder()
                .character(character)
                .gradeLevel(gradeLevel)
                .strokeCount(strokeCount)
                .jlptLevel(jlptLevel)
                .radicals(radicals)
                .readingVietnam(readingVietnam)
                .readingJapaneseOn(readingJapaneseOn)
                .readingJapaneseKun(readingJapaneseKun)
                .meanings(meaning)
                .svgFile(svgFile)
                .build();

        assertThat(kanji).isNotNull();
        assertThat(kanji.getCharacter()).isEqualTo(character);
        assertThat(kanji.getGradeLevel()).isEqualTo(gradeLevel);
        assertThat(kanji.getStrokeCount()).isEqualTo(strokeCount);
        assertThat(kanji.getJlptLevel()).isEqualTo(jlptLevel);
        assertThat(kanji.getRadicals()).isEqualTo(radicals);
        assertThat(kanji.getReadingVietnam()).isEqualTo(readingVietnam);
        assertThat(kanji.getReadingJapaneseOn()).isEqualTo(readingJapaneseOn);
        assertThat(kanji.getReadingJapaneseKun()).isEqualTo(readingJapaneseKun);
        assertThat(kanji.getMeanings()).isEqualTo(meaning);
        assertThat(kanji.getSvgFile()).isEqualTo(svgFile);

    }
}
