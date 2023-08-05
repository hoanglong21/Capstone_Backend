package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.impl.TranslateServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class TranslateServiceTest {

    @Autowired
    private TranslateServiceImpl translateService;

    @Order(1)
    @Test
    public void translateClients5() {
        try {
            String translate = translateService.translateClients5("Wellcome back to my website", "ja");
            assertThat(translate != null && !translate.equals("")).isTrue();
        } catch (ResourceNotFroundException e) {
            assertThat(e.getMessage()).isEqualTo("No translation found.");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("GET request failed")).isTrue();
        }
    }

    @Order(2)
    @Test
    public void translateGoogleapis() {
        try {
            String translate = translateService.translateGoogleapis("Wellcome back to my website", "ja");
            assertThat(translate != null && !translate.equals("")).isTrue();
        } catch (ResourceNotFroundException e) {
            assertThat(e.getMessage()).isEqualTo("No translation found.");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("GET request failed")).isTrue();
        }
    }

    @Order(3)
    @Test
    public void translateMymemory() {
        try {
            String translate = translateService.translateMymemory("Wellcome back to my website", "ja");
            assertThat(translate != null && !translate.equals("")).isTrue();
        } catch (ResourceNotFroundException e) {
            assertThat(e.getMessage()).isEqualTo("No translation found.");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("GET request failed")).isTrue();
        }
    }
}
