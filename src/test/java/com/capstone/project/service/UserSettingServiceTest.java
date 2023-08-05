package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
import com.capstone.project.repository.ClassLearnerRepository;
import com.capstone.project.repository.UserSettingRepository;
import com.capstone.project.service.impl.ClassLeanerServiceImpl;
import com.capstone.project.service.impl.UserSettingServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserSettingServiceTest {

    @Mock
    private UserSettingRepository userSettingRepository;

    @InjectMocks
    private UserSettingServiceImpl userSettingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Order(1)
    @Test
    public void testGetAllUserSetting() {
        try {
            UserSetting userSetting = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value("ja")
                    .build();

            List<UserSetting> userSettings = List.of(userSetting);
            when(userSettingRepository.findAll()).thenReturn(userSettings);
            assertThat(userSettingService.getAllUserSetting().size()).isGreaterThan(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Order(2)
    @Test
    void testGetUserSettingById() {
        try {

            UserSetting userSetting = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value("ja")
                    .build();

            when(userSettingRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(userSetting));
            UserSetting getUserSetting = userSettingService.getUserSettingById(1);
            assertThat(getUserSetting).isEqualTo(userSetting);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(3)
    @Test
    void testGetUserSettingByUserId() {
        List<UserSetting> userSettings = new ArrayList<>();
        UserSetting userSetting1 = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value("ja")
                .build();
        UserSetting userSetting2 = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value("vi")
                .build();
        userSettings.add(userSetting1);
        userSettings.add(userSetting2);
        when(userSettingRepository.getByUserId(any(Integer.class))).thenReturn(userSettings);
        List<UserSetting> retrievedUserSetting = userSettingService.getUserSettingByUserId(1);
        assertThat(retrievedUserSetting).isEqualTo(userSettings);
    }

    @Order(4)
    @Test
    void testGetUserSettingBySettingId() {
        List<UserSetting> userSettings = new ArrayList<>();
        UserSetting userSetting1 = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value("ja")
                .build();
        UserSetting userSetting2 = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value("vi")
                .build();
        userSettings.add(userSetting1);
        userSettings.add(userSetting2);
        when(userSettingRepository.getBySettingId(any(Integer.class))).thenReturn(userSettings);
        List<UserSetting> retrievedUserSetting = userSettingService.getUserSettingBySettingId(1);
        assertThat(retrievedUserSetting).isEqualTo(userSettings);
    }

    @Order(5)
    @ParameterizedTest(name = "index => userId={0}, settingId={1}, value{2}")
    @CsvSource({
            "1, 1, ja ",
            "2, 1 , vi "
    })
    public void testCreateUserSetting(int userId, int classId, String value) {
        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value(value)
                .build();
        when(userSettingRepository.save(any())).thenReturn(userSetting);

        UserSetting createdusersetting = userSettingService.createUserSetting(userSetting);
        assertThat(userSetting).isEqualTo(createdusersetting);
    }

    @Order(6)
    @ParameterizedTest(name = "index => userId={0}, settingId={1}, value{2}")
    @CsvSource({
            "1, 1, ja ",
            "2, 1 , vi "
    })
    public void testUpdateUserSetting(int userId, int classId, String value) {
        try {
            UserSetting userSetting_new = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value("ja")
                    .build();
            UserSetting userSetting = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value(value)
                    .build();
            when(userSettingRepository.findById(any())).thenReturn(Optional.ofNullable(userSetting_new));
            when(userSettingRepository.save(any())).thenReturn(userSetting);

            UserSetting created_usersetting = userSettingService.updateUserSetting(userSetting, 1);
            assertThat(created_usersetting).isEqualTo(userSetting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Order(7)
    @Test
    void testDeleteUserSetting() {

        UserSetting userSetting = UserSetting.builder()
                .user(User.builder().id(1).build())
                .setting(Setting.builder().id(1).build())
                .value("ja")
                .build();

        when(userSettingRepository.findById(any())).thenReturn(Optional.ofNullable(userSetting));
        doNothing().when(userSettingRepository).delete(userSetting);
        try {
            userSettingService.deleteUserSetting(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(userSettingRepository, times(1)).delete(userSetting);
    }

    @Order(8)
    @Test
    void testCustomGetUserSettingByUserId() {
        try {
            // Tạo danh sách cài đặt người dùng tùy chỉnh
            List<UserSetting> userSettings = new ArrayList<>();
            UserSetting userSetting1 = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value("ja")
                    .build();
            UserSetting userSetting2 = UserSetting.builder()
                    .user(User.builder().id(1).build())
                    .setting(Setting.builder().id(1).build())
                    .value("vn")
                    .build();
            userSettings.add(userSetting1);
            userSettings.add(userSetting2);

            Map<String, String> result = userSettingService.CustomGetUserSettingByUserId(1);

            assertThat(result.get("language")).isEqualTo("vn");
            assertThat(result.get("study reminder")).isEqualTo("07:00");
            assertThat(result.get("assignment due date reminder")).isEqualTo("24");
            assertThat(result.get("set added")).isEqualTo("TRUE");
            assertThat(result.get("post added")).isEqualTo("TRUE");
            assertThat(result.get("assignment assigned")).isEqualTo("TRUE");
            assertThat(result.get("test assigned")).isEqualTo("TRUE");
            assertThat(result.get("submission graded")).isEqualTo("TRUE");
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(9)
    @Test
    public void testsaveUserSettingCustom() {

        int userId = 1;
        int settingId = 1;
        String validTimeFormat = "07:00";

        UserSetting expectedUserSetting = UserSetting.builder()
                .value(validTimeFormat)
                .user(User.builder().id(userId).build())
                .setting(Setting.builder().id(settingId).build())
                .build();

        when(userSettingRepository.getUserSettingCustom(userId, settingId)).thenReturn(null);
        when(userSettingRepository.save(any(UserSetting.class))).thenReturn(expectedUserSetting);

        UserSetting result = userSettingService.saveUserSettingCustom(userId, settingId, validTimeFormat);
        assertNotNull(result);
        assertEquals(validTimeFormat, result.getValue());

        verify(userSettingRepository, times(1)).save(any(UserSetting.class));
    }

}

