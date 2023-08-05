package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Notification;
import com.capstone.project.model.Setting;
import com.capstone.project.model.User;
import com.capstone.project.repository.NotificationRepository;
import com.capstone.project.repository.SettingRepository;
import com.capstone.project.service.impl.NotificationServiceImpl;
import com.capstone.project.service.impl.SettingServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SettingServiceTest {

    @Mock
    private SettingRepository settingRepository;

    @InjectMocks
    private SettingServiceImpl settingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    void testGetAllSetting() {
        Setting setting = Setting.builder()
                .title("language")
                .build();
        List<Setting> settings = List.of(setting);
        when(settingRepository.findAll()).thenReturn(settings);
        assertThat(settingService.getAllSetting().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testSettingById() {
        Setting setting = Setting.builder()
                .title("language")
                .build();
        when(settingRepository.findById(any())).thenReturn(Optional.ofNullable(setting));
        try{
           Setting getSetting = settingService.getSettingById(1);
            assertThat(getSetting).isEqualTo(setting);
        }catch (ResourceNotFroundException e){
            e.printStackTrace();
        }
    }

    @Order(3)
    @ParameterizedTest(name = "index => title{0}")
    @CsvSource({
            " studyset ",
            " language "
    })
    public void testCreateSetting(String title){
        Setting setting = Setting.builder()
                .title(title)
                .build();
        when(settingRepository.save(any())).thenReturn(setting);
        Setting createdsetting = settingService.createSetting(setting);
        assertThat(setting).isEqualTo(createdsetting);
    }

    @Order(4)
    @ParameterizedTest(name = "index => title{0}")
    @CsvSource({
            " studyset ",
            " language "
    })
    void testUpdateSetting(String title) {
        try{

            Setting setting_new = Setting.builder()
                    .title("language")
                    .build();

            Setting setting = Setting.builder()
                    .title(title)
                    .build();
            when(settingRepository.findById(any())).thenReturn(Optional.ofNullable(setting_new));
            when(settingRepository.save(any())).thenReturn(setting);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Order(5)
    @Test
    void testDeleteSetting() {

        Setting setting = Setting.builder()
                .title("language")
                .build();

        when(settingRepository.findById(any())).thenReturn(Optional.ofNullable(setting));
        doNothing().when(settingRepository).delete(setting);
        try {
            settingService.deleteSetting(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(settingRepository, times(1)).delete(setting);
    }
}
