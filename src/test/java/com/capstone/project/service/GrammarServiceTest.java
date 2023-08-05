package com.capstone.project.service;

import com.capstone.project.model.Grammar;
import com.capstone.project.service.impl.GrammarServiceImpl;
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
public class GrammarServiceTest {

    @Autowired
    private GrammarServiceImpl grammarService;

    private List<Grammar> grammarList;

    @Order(1)
    @Test
    public void initGrammar() {
        grammarList = grammarService.initGrammars();
        assertThat(grammarList.size()).isGreaterThan(0);
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => query={0}, level={1}, page={2}, pageSize={3}, greaterThanZero={4}")
    @CsvSource({
            "禁じ得, 0, 1, 20, true",
            "亜亜, 1, 1, 20, false",
    })
    public void searchAndPaginate(String query, int level, int page, int pageSize, boolean greaterThanZero) {
        List<Grammar> list = (List<Grammar>) grammarService.searchAndPaginate(query, level, page, pageSize).get("list");
        assertThat(list.size()>0).isEqualTo(greaterThanZero);
    }

    @Order(3)
    @Test
    public void getGrammarList() {
        assertThat(grammarService.getGrammarList().size()).isGreaterThan(0);
    }
}
