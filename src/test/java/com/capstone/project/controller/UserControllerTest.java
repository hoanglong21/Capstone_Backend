package com.capstone.project.controller;

import com.capstone.project.dto.UserRequest;
import com.capstone.project.dto.UserUpdateRequest;
import com.capstone.project.model.User;
import com.capstone.project.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.assertj.core.api.Assertions;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, modelMapper)).build();
        userController = new UserController(userService, modelMapper);
    }

    @Order(1)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}")
    @CsvSource({
            "long, Hoang, Long, Male, 2001-11-21, long@gmail.com, 0352269303, 123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg",
            "tuyet, Nguyen, Tuyet, Female, 2001-09-27, tuyet@gmail.com, 0352269304, 123456, ROLE_ADMIN, HG, Hello, pending, avatar2.png"
    })
    void testGetUser(String username, String first_name, String last_name, String gender, String date, String email,
                     String phone, String password, String role, String address, String bio, String status, String avatar)
            throws Exception {
        // make stub
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        User user = User.builder()
                .username(username)
                .first_name(first_name)
                .last_name(last_name)
                .gender(gender)
                .dob(dateFormat.parse(date))
                .email(email)
                .phone(phone)
                .password(password)
                .role(role)
                .address(address)
                .bio(bio)
                .status(status)
                .avatar(avatar)
                .build();

        when(userService.getUserByUsername(username)).thenReturn(user);

        // test
        mockMvc.perform(get("/api/v1/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.first_name").value(user.getFirst_name()))
                .andExpect(jsonPath("$.last_name").value(user.getLast_name()))
                .andExpect(jsonPath("$.gender").value(user.getGender()))
                .andExpect(jsonPath("$.dob").value(user.getDob()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.role").value(user.getRole()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andExpect(jsonPath("$.bio").value(user.getBio()))
                .andExpect(jsonPath("$.status").value(user.getStatus()))
                .andExpect(jsonPath("$.avatar").value(user.getAvatar()));
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}, success={13}")
    @CsvSource({
            "test_long1, Hoang, Long, Male, 2001-11-21, test_long1   @gmail.com, 0352269309, Long123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg, false",
            "test_long2, Hoang, Long, Male, 2001-11-21, test_long2@gmail.com, 0352269308, Long123456, ROLE_TUTOR, HB, Hello, pending, avatar2.jpg, true",
    })
    void testUpdateUser(String username, String first_name, String last_name, String gender, String date, String email,
                        String phone, String password, String role, String address, String bio, String status, String avatar, boolean success)
            throws Exception {
        // make stub
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        User user = User.builder()
                .username(username)
                .first_name("Hoang")
                .last_name("Long")
                .gender("Male")
                .dob(dateFormat.parse("2001-11-11"))
                .email("long@gmail.com")
                .phone("0352269303")
                .password("Long123456")
                .role("ROLE_LEARNER")
                .address("HN")
                .bio("Swag")
                .status("active")
                .avatar("avatar.jpg")
                .build();

        UserUpdateRequest userDetails = UserUpdateRequest.builder()
                .gender(gender)
                .dob(dateFormat.parse(date))
                .phone(phone)
                .address(address)
                .bio(bio)
                .avatar(avatar)
                .build();
        // TODO change a bit

        userDetails.setFirst_name(first_name);
        userDetails.setLast_name(last_name);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        when(userService.updateUser(any(), any())).thenReturn(user);
        when(bindingResult.hasErrors()).thenReturn(!success);

        ResponseEntity<?> response = userController.updateUser(username, userDetails, bindingResult);
        if(success) {
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        } else {
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

    }


    @Order(3)
    @ParameterizedTest(name = "{index} => excludedName = {0}, expectedNumber = {1}")
    @CsvSource({
            "Long1, 1",
            "Short, 2",
            "Long2, 1",
            "VeryLongName, 2",
    })
    void testFindAllNameExcept(String excludedName, int expectedNumber) throws Exception {
        // make stub
        List<User> names = new ArrayList<>();
        names.add(User.builder().username("Long1").build());
        names.add(User.builder().username("Long2").build());

        names.remove(excludedName);

        when(userService.findAllNameExcept("", excludedName)).thenReturn(names);

        // test
        mockMvc.perform(get("/api/v1/otherusers/{username}", excludedName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(expectedNumber)));

    }

    @Order(4)
    @ParameterizedTest(name = "{index} => username={0}, isBanSuccess={1}")
    @CsvSource({
            "test_long, true",
            "test_long, false",
    })
    void testBanUser(String username,boolean isBanSuccess) throws Exception {
        when(userService.banUser(username)).thenReturn(isBanSuccess);
        mockMvc.perform(get("/api/v1/users/{username}/ban", username))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.equalTo(String.valueOf(isBanSuccess))));

    }

    @Order(5)
    @ParameterizedTest(name = "{index} => username={0}, isDeleteSuccess={1}")
    @CsvSource({
            "test_long, true",
            "test_long, false",
    })
    void testDeleteUser(String username,boolean isDeleteSuccess) throws Exception {
        when(userService.deleteUser(username)).thenReturn(isDeleteSuccess);
        mockMvc.perform(delete("/api/v1/users/{username}/delete", username))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.equalTo(String.valueOf(isDeleteSuccess))));
    }

    @Order(6)
    @ParameterizedTest(name = "{index} => username={0}, email={1}, status={2}, isBannedDateMoreThan7Days={3}")
    @CsvSource({
            "test_long, test_long@gmail.com, active, false",
            "test_long, test_long@gmail.com, delete, true",
            "test_long, test_long@gmail.com, delete, false"
    })
    void testRecoverUser(String username, boolean isBannedDateMoreThan7Days) throws Exception {

        when(userService.recoverUser(username)).thenReturn(isBannedDateMoreThan7Days);
        mockMvc.perform(get("/api/v1/users/{username}/recover", username))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.equalTo(String.valueOf(isBannedDateMoreThan7Days))));
    }


}