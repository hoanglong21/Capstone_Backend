package com.capstone.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.capstone.project.exception.DuplicateValueException;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.impl.UserServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // For stub
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private JdbcTemplate jdbcTemplate;

    // For actual test
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}," +
            " email={3}, role={4}, isUsernameExists={5}, isEmailExists={6}")
    @CsvSource({
            "test_long, Hoang, Long, test_long@gmail.com, ROLE_LEARNER, false, false",
            "test_long, Hoang, Long, test_long2@gmail.com, ROLE_TUTOR, true, false",
            "test_long3, Hoang, Long, test_long@gmail.com, ROLE_LEARNER, false, true",
            "test_long4, Hoang, Long, test_long4@gmail.com, ROLE_TUTOR, false, false"
    })
    void testCreateUser(String username, String first_name, String last_name,
                        String email, String role, boolean isUsernameExists, boolean isEmailExists) {
        User user = User.builder()
                .username(username)
                .first_name(first_name)
                .last_name(last_name)
                .email(email)
                .role(role)
                .build();

        when(userRepository.existsByUsername(any())).thenReturn(isUsernameExists);
        when(userRepository.existsByEmail(any())).thenReturn(isEmailExists);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // actual test
        try {
            User createdUser = userServiceImpl.createUser(user);
            assertEquals(user.getUsername(), createdUser.getUsername());
            assertEquals(passwordEncoder.encode(user.getPassword()), createdUser.getPassword());
            assertEquals(user.getEmail(), createdUser.getEmail());
            assertEquals(user.getFirst_name(), createdUser.getFirst_name());
            assertEquals(user.getLast_name(), createdUser.getLast_name());
            assertEquals(user.getRole(), createdUser.getRole());
            assertEquals(user.getPhone(), createdUser.getPhone());
        } catch (DuplicateValueException e) {
            if(isUsernameExists) {
                assertEquals("Username already registered", e.getMessage());
            }
            if(isEmailExists) {
                assertEquals("Email already registered", e.getMessage());
            }
        }
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => excludedName = {0}, expectedNumber = {1}")
    @CsvSource({
            "Long1, 1",
            "Short, 2",
            "Long2, 1",
            "VeryLongName, 2",
    })
    void testFindAllNameExcept(String excludedName, int expectedNumber) {
        List<User> names = new ArrayList<>();
        names.add(User.builder().username("Long1").build());
        names.add(User.builder().username("Long2").build());

        names.remove(excludedName);

        when(userRepository.findAllNameExcept("",excludedName)).thenReturn(names);

        List<User> result = userServiceImpl.findAllNameExcept("", excludedName);
        Assertions.assertThat(result.size()).isEqualTo(expectedNumber);
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}")
    @CsvSource({
            "long, Hoang, Long, Male, 2001-11-21, long@gmail.com, 0352269303, 123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg",
            "tuyet, Nguyen, Tuyet, Female, 2001-09-27, tuyet@gmail.com, 0352269304, 123456, ROLE_ADMIN, HG, Hello, pending, avatar2.png"
    })
    void testGetUserByUsername(String username, String first_name, String last_name, String gender, String date, String email,
                               String phone, String password, String role, String address, String bio, String status, String avatar) {
        User user = null;
        try {
            user = User.builder()
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(userRepository.findUserByUsername("long")).thenReturn(user);

        User result = null;
        try {
            result = userServiceImpl.getUserByUsername("long");
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }

        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getFirst_name()).isEqualTo(first_name);
        assertThat(result.getLast_name()).isEqualTo(last_name);
        assertThat(result.getGender()).isEqualTo(gender);
        assertThat(result.getDob()).isEqualTo(date);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPhone()).isEqualTo(phone);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result.getRole()).isEqualTo(role);
        assertThat(result.getAddress()).isEqualTo(address);
        assertThat(result.getBio()).isEqualTo(bio);
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getAvatar()).isEqualTo(avatar);
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}")
    @CsvSource({
            "test_long, Hoang, Long, Male, 2001-11-21, test_long@gmail.com, 0352269303, 123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg",
            "test_long, Hoang, Long, Male, 2001-11-21, test_long@gmail.com, 0352269303, 123456, ROLE_TUTOR, HB, Hello, pending, avatar2.jpg",

    })
    void testUpdateUser(String username, String first_name, String last_name, String gender, String date, String email,
                        String phone, String password, String role, String address, String bio, String status, String avatar) {
        User user = null;
        try {
            user = User.builder()
                    .username("test_long")
                    .first_name("Hoang")
                    .last_name("Long")
                    .gender("Male")
                    .dob(dateFormat.parse("2001-11-21"))
                    .email("long@gmail.com")
                    .phone("0352269303")
                    .password("123456")
                    .role("ROLE_LEARNER")
                    .address("HN")
                    .bio("Swag")
                    .status("active")
                    .avatar("avatar.jpg")
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        User userDetails = null;
        try {
            userDetails = User.builder()
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
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        when(userRepository.findUserByUsername("long")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(userDetails);

        User updatedUser = null;
        try {
            updatedUser = userServiceImpl.updateUser("long", userDetails);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }

        assertThat(updatedUser.getUsername()).isEqualTo(username);
        assertThat(updatedUser.getFirst_name()).isEqualTo(first_name);
        assertThat(updatedUser.getLast_name()).isEqualTo(last_name);
        assertThat(updatedUser.getGender()).isEqualTo(gender);
        assertThat(updatedUser.getDob()).isEqualTo(date);
        assertThat(updatedUser.getEmail()).isEqualTo(email);
        assertThat(updatedUser.getPhone()).isEqualTo(phone);
        assertThat(updatedUser.getPassword()).isEqualTo(password);
        assertThat(updatedUser.getRole()).isEqualTo(role);
        assertThat(updatedUser.getAddress()).isEqualTo(address);
        assertThat(updatedUser.getBio()).isEqualTo(bio);
        assertThat(updatedUser.getStatus()).isEqualTo(status);
        assertThat(updatedUser.getAvatar()).isEqualTo(avatar);


    }

    @Order(5)
    @ParameterizedTest(name = "{index} => username={0}, email={1}, status={2}")
    @CsvSource({
            "test_long, test_long@gmail.com, active",
            "test_long, test_long@gmail.com, delete",
    })
    void testBanUser(String username, String email, String status) {
        User user = User.builder()
                .username(username)
                .email(email)
                .status(status)
                .build();

        when(userRepository.findUserByUsername(username)).thenReturn(user);

        try {
            Assertions.assertThat(userServiceImpl.banUser(username)).isTrue();
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThat(user.getStatus()).isEqualTo("banned");
        Assertions.assertThat(user.getBanned_date()).isNotNull();
    }

    @Order(6)
    @ParameterizedTest(name = "{index} => username={0}, email={1}, status={2}")
    @CsvSource({
            "test_long, test_long@gmail.com, active",
            "test_long, test_long@gmail.com, delete",

    })
    void testDeleteUser(String username, String email, String status) {
        User user = User.builder()
                .username(username)
                .email(email)
                .status(status)
                .build();

        when(userRepository.findUserByUsername("long")).thenReturn(user);

        try {
            Assertions.assertThat(userServiceImpl.deleteUser("long")).isTrue();
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThat(user.getStatus()).isEqualTo("deleted");
        Assertions.assertThat(user.getDeleted_date()).isNotNull();
    }

    @Order(7)
    @ParameterizedTest(name = "{index} => username={0}, email={1}, status={2}, isBannedDateMoreThan7Days={3}")
    @CsvSource({
            "test_long, test_long@gmail.com, active, false",
            "test_long, test_long@gmail.com, delete, true",
            "test_long, test_long@gmail.com, delete, false"

    })
    void testRecoverUser(String username, String email, String status, boolean isBannedDateMoreThan7Days) {
        Date date;
        if(isBannedDateMoreThan7Days) {
            date = new Date(System.currentTimeMillis() - 8*24*60*60*1000);
        } else {
            date = new Date(System.currentTimeMillis() - 6*24*60*60*1000);
        }
        User user = User.builder()
                .username(username)
                .status(email)
                .banned_date(date)
                .status(status)
                .build();

        when(userRepository.findUserByUsername(username)).thenReturn(user);

        if(isBannedDateMoreThan7Days) {
            try {
                Assertions.assertThat(userServiceImpl.recoverUser(username)).isTrue();
            } catch (ResourceNotFroundException e) {
                throw new RuntimeException(e);
            }
            Assertions.assertThat(user.getStatus()).isEqualTo("active");
        } else {
            try {
                Assertions.assertThat(userServiceImpl.recoverUser(username)).isFalse();
            } catch (ResourceNotFroundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Order(8)
    @ParameterizedTest(name = "{index} => username={0}, firstName={1}, lastName={2}, email={3}, token={4}, expected={5}")
    @CsvSource({
            "long, hoang, long, test_long@gmail.com, 0123456, true",
            "nonexistentuser, , , , , false",
    })
    void sendVerificationEmail(String username, String firstName, String lastName, String email, String token, Boolean expected) throws ResourceNotFroundException {
        User user = User.builder()
                .username(username)
                .first_name(firstName)
                .last_name(lastName)
                .email(email)
                .token(token)
                .build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(user);

        if(expected) {
            when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
            doNothing().when(mailSender).send(any(MimeMessage.class));
        } else {
            doThrow(new RuntimeException("Failed to send email")).when(mailSender).send(any(MimeMessage.class));
        }
        Boolean result = userServiceImpl.sendVerificationEmail(username);

        assertThat(result).isEqualTo(expected);
    }

    @Order(9)
    @ParameterizedTest(name = "{index} => username={0}, pin={1}, password={2}, userFound={3}, expected={4}")
    @CsvSource({
            "testuser, 1234, newPassword, true, true", // Successful password reset
            "testuser, wrongpin, newPassword, true, false", // Incorrect pin
            "nonexistentuser, 1234, newPassword, false, false", // User not found
    })
    void testResetPassword(String username, String pin, String password, boolean userFound, boolean expected) throws ResourceNotFroundException {
        User user = null;
        if (userFound) {
            user = User.builder()
                    .username(username)
                    .pin("1234")
                    .build();
        }

        // Mock the userRepository behavior
        when(userRepository.findUserByUsername(username)).thenReturn(user);

        if (userFound && user.getPin().equals(pin)) {
            when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        }

        Boolean result = false;
        try {
            result = userServiceImpl.resetPassword(username, pin, password);
        } catch (ResourceNotFroundException e) {
            assertThat(userFound).isFalse();
        }

        assertThat(result).isEqualTo(expected);
        verify(userRepository, times(1)).findUserByUsername(username);

        if (userFound) {
            if (user.getPin().equals(pin)) {
                verify(passwordEncoder, times(1)).encode(password);
                verify(userRepository, times(1)).save(user);
            } else {
                verify(passwordEncoder, never()).encode(anyString());
                verify(userRepository, never()).save(any(User.class));
            }
        } else {
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Order(10)
    @ParameterizedTest(name = "{index} => username={0}, firstName={1}, lastName={2}, email={3}, token={4}, expected={5}")
    @CsvSource({
            "long, hoang, long, test_long@gmail.com, 0123456, true",
            "nonexistentuser, , , , , false",
    })
    void sendResetPasswordEmail(String username, String firstName, String lastName, String email, String token, Boolean expected) throws ResourceNotFroundException {
        User user = User.builder()
                .username(username)
                .first_name(firstName)
                .last_name(lastName)
                .email(email)
                .token(token)
                .build();

        when(userRepository.findUserByUsername(anyString())).thenReturn(user);

        if(expected) {
            when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
            doNothing().when(mailSender).send(any(MimeMessage.class));
        } else {
            doThrow(new RuntimeException("Failed to send email")).when(mailSender).send(any(MimeMessage.class));
        }
        String result = userServiceImpl.sendResetPasswordEmail(username);

        assertThat(!result.equals("Error occur")).isEqualTo(expected);
    }

    @Order(11)
    @ParameterizedTest(name = "{index} => username={0}, providedPassword={1}, userPassword={2}, expected={3}")
    @CsvSource({
            "testuser, password, encodedPassword, true", // Successful password match
            "testuser, incorrectPassword, encodedPassword, false", // Password mismatch
    })
    void testCheckMatchPassword(String username, String providedPassword, String userPassword, boolean expected) {
        User user = User.builder()
                .username(username)
                .password(userPassword) // User's actual encoded password
                .build();

        when(userRepository.findUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(providedPassword, userPassword)).thenReturn(expected);
        Boolean result = null;
        try {
            result = userServiceImpl.checkMatchPassword(username, providedPassword);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isEqualTo(expected);
    }

    @Order(12)
    @ParameterizedTest(name = "{index} => username={0}, newPassword={1}, expected={2}")
    @CsvSource({
            "testuser, newPassword, true", // Password change success
    })
    void testChangePassword(String username, String newPassword, boolean expected) {
        User user = User.builder()
                .username(username)
                .password("oldEncodedPassword") // User's current encoded password
                .build();

        when(userRepository.findUserByUsername(username)).thenReturn(user);
        String encodedNewPassword = "encodedNewPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(userRepository.save(user)).thenReturn(user);

        // Call the method being tested
        Boolean result = null;
        try {
            result = userServiceImpl.changePassword(username, newPassword);
        } catch (ResourceNotFroundException e) {
            throw new RuntimeException(e);
        }

        assertThat(result).isEqualTo(expected);
    }

    @Order(13)
    @ParameterizedTest(name = "{index} => name={0}, username={1}, email={2}, gender={3}, phone={4}, role={5}, address={6}, bio={7}, status={8}, " +
            "fromDob={9}, toDob={10}, fromBanned={11}, toBanned={12}, fromDeleted={13}, toDeleted={14}, fromCreated={15}, toCreated={16}, " +
            "sortBy={17}, direction={18}, page={19}, size={20}")
    @CsvSource({
            "Hoang, , long@gmail.com, , , , , , , , , , , , , , , , , 1, 5",
            ", , long@gmail.com, , , , , , , , , , , , , , , , , 1, 5",
    })
    void testFilterUser(String name, String username, String email, String gender, String phone, String role, String address, String bio, String status,
                        String fromDob, String toDob, String fromBanned, String toBanned, String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                        String sortBy, String direction, int page, int size) {

        MockitoAnnotations.openMocks(this);
        List<User> userList = Arrays.asList(
                User.builder().last_name("Hoang").first_name("Long").email("long@gmail.com").build(),
                User.builder().last_name("Hoang").first_name("Long1").email("long@gmail.com").build()
        );

        when(jdbcTemplate.queryForObject(any(String.class), any(Class.class), any())).thenReturn(2L);
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class))).thenReturn(userList);
        Map<String, Object> result = userServiceImpl.filterUser(name, username, email, gender, phone, new String[]{role}, address, bio, new String[]{status},
                fromDob, toDob, fromBanned, toBanned, fromDeleted, toDeleted, fromCreated, toCreated, sortBy, direction, page, size);
        assertThat(result.get("list")).isEqualTo(userList);
    }



}