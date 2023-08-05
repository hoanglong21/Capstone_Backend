package com.capstone.project.service;


import com.capstone.project.model.Kanji;
import com.capstone.project.service.impl.KanjiServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class KanjiServiceTest {

    @Autowired
    private KanjiServiceImpl kanjiService;

    private List<Kanji> kanjiList;

    @Order(1)
    @Test
    public void initKanji() {
        kanjiList = kanjiService.initKanji();
        assertThat(kanjiList.size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => query={0}, page={1}, pageSize={2}, greaterThanZero={3}")
    @CsvSource({
            "亜, 1, 20, true",
            "亜亜, 1, 20, false",
    })
    public void searchAndPaginate(String query, int page, int pageSize, boolean greaterThanZero) {
        List<Kanji> list = (List<Kanji>) kanjiService.searchAndPaginate(query, page, pageSize).get("list");
        assertThat(list.size()>0).isEqualTo(greaterThanZero);
    }

    @Order(3)
    @Test
    public void getVocabularyList() {
        assertThat(kanjiService.getKanjiList().size()).isGreaterThan(0);
    }
}
