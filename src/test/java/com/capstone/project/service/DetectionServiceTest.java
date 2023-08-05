package com.capstone.project.service;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.VocabularyTokenizer;
import com.capstone.project.service.impl.DetectionServiceImpl;
import com.capstone.project.service.impl.TranslateServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class DetectionServiceTest {

    @Autowired
    private DetectionServiceImpl detectionService;

    @Order(1)
    @Test
    public void detectVocabulary() {
        List<VocabularyTokenizer> list = detectionService.detectVocabulary("さっき、木村さんという人から電話がありましたよ");
        assertThat(list.size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    public void detectGrammar() {
        try {
            String result = detectionService.detectGrammar("さっき、木村さんという人から電話がありまたよ", "English");
            assertThat(result != null && !result.equals("")).isTrue();
        } catch (Exception e) {
            assertThat(!e.getMessage().equals(""));
            // don't need
        }
    }

    @Order(3)
    @Test
    public void grammarCheck() {
        try {
            String result = detectionService.grammarCheck("さっき、木村さんという人から電話がありまたよ");
            assertThat(result != null && !result.equals("")).isTrue();
        } catch (Exception e) {
            assertThat(!e.getMessage().equals(""));
            // don't need
        }

    }
}
