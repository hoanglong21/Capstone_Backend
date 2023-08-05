package com.capstone.project.repository;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    private PasswordEncoder passwordEncoder;

    // JUnit test for saveUser
    @Order(1)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, " +
            " last_name={2}, email={3}, role={4}, expected={5}")
    @CsvSource({
            "test_long, Hoang, Long, test_long@gmail.com, ROLE_LEARNER, true",
            "test_long, Hoang, Long, test_long2@gmail.com, ROLE_TUTOR, false",
            "test_long3, Hoang, Long, test_long@gmail.com, ROLE_LEARNER, false",
            "test_long4, Hoang, Long, test_long4@gmail.com, ROLE_TUTOR, true"
    })
    public void testSaveUser(String username, String first_name, String last_name,
                             String email, String role, boolean expected) {
        User user = User.builder()
                .username(username)
                .first_name(first_name)
                .last_name(last_name)
                .email(email)
                .role(role)
                .build();
        if (expected) {
            userRepository.save(user);
            Assertions.assertThat(user.getId()).isGreaterThan(0);
        } else {
            Assertions.assertThat(user.getId()).isEqualTo(0);
        }
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => username={0}," +
            " email={1}, find = {2}, expected={3}")
    @CsvSource({
            "test_long, test_long@gmail.com, test_long, true",
            "test_long2, test_long2@gmail.com, test_long0, false",
    })
    public void testFindUserByUsername(String username, String email, String find, boolean expected) {
        User user = User.builder()
                .username(username)
                .email(email)
                .build();

        userRepository.save(user);

        User findUser = userRepository.findUserByUsername(find);
        Assertions.assertThat(findUser != null).isEqualTo(expected);
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => username={0}," +
            " email={1}, find = {2}, expected={3}")
    @CsvSource({
            "test_long, test_long@gmail.com, test_long@gmail.com, true",
            "test_long2, test_long2@gmail.com, test_long0@gmail.com, false",
    })
    public void testFindUserByEmail(String username, String email, String find, boolean expected) {
        User user = User.builder()
                .username(username)
                .email(email)
                .build();

        userRepository.save(user);

        User findUser = userRepository.findUserByEmail(find);
        Assertions.assertThat(findUser != null).isEqualTo(expected);
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => id={0}, expected={1}")
    @CsvSource({
            "1, true",
            "0, false"
    })
    public void testFindUserById(int id, boolean expected) {
        User findUser = userRepository.findUserById(id);
        System.out.println(findUser);
        Assertions.assertThat(findUser != null).isEqualTo(expected);
    }

    @Order(5)
    @ParameterizedTest(name = "{index} => find = {0}")
    @CsvSource({
            "test_long", "test_long0",
    })
    public void testFindAllNameExcept(String find) {
        List<User> userList = Arrays.asList(
                User.builder().username("test_long").email("test_long@gmail.com").build(),
                User.builder().username("test_long2").email("test_long2@gmail.com").build()
        );

        userRepository.saveAll(userList);

        List<User> names = userRepository.findAllNameExcept("", find);
        List<String> strings = new ArrayList<>();
        for(User s: names) {
            strings.add(s.getUsername());
        }
        Assertions.assertThat(strings).doesNotContain(find);
    }


    @Order(6)
    @ParameterizedTest(name = "{index} => username = {0}, expected={1}")
    @CsvSource({
            "test_long, true",
            "test_long0, false",
    })
    public void testExistsByUsername(String find, boolean expected) {
        List<User> userList = Arrays.asList(
                User.builder().username("test_long").email("test_long@gmail.com").build(),
                User.builder().username("test_long2").email("test_long2@gmail.com").build()
        );

        userRepository.saveAll(userList);

        boolean exists = userRepository.existsByUsername(find);
        Assertions.assertThat(exists).isEqualTo(expected);
    }


    @Order(7)
    @ParameterizedTest(name = "{index} => phone = {0}, expected={1}")
    @CsvSource({
            "03052269303, true",
            "01010101010, false",
    })
    public void testExistsByPhone(String phone, boolean expected) {
        List<User> userList = Arrays.asList(
                User.builder().username("test_long").email("test_long@gmail.com").phone("03052269303").build(),
                User.builder().username("test_long2").email("test_long2@gmail.com").phone("03052269304").build()
        );

        userRepository.saveAll(userList);

        boolean exists = userRepository.existsByPhone(phone);
        Assertions.assertThat(exists).isEqualTo(expected);
    }


    @Order(8)
    @ParameterizedTest(name = "{index} => email = {0}, expected={1}")
    @CsvSource({
            "test_long@gmail.com, true",
            "test_nobody@gmail.com, false",
    })
    public void testExistsByEmail(String email, boolean expected) {
        List<User> userList = Arrays.asList(
                User.builder().username("test_long").email("test_long@gmail.com").build(),
                User.builder().username("test_long2").email("test_long2@gmail.com").build()
        );

        userRepository.saveAll(userList);

        boolean exists = userRepository.existsByEmail(email);
        Assertions.assertThat(exists).isEqualTo(expected);
    }


    @Order(9)
    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}")
    @CsvSource({
            "test_long, Hoang, Long, Male, 2001-11-21, test_long@gmail.com, 0352269303, 123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg",
            "test_long, Hoang, Long, Male, 2001-11-21, test_long@gmail.com, 0352269303, 123456, ROLE_TUTOR, HB, Hello, pending, avatar2.jpg",

    })
    public void testUpdateUser(String username, String first_name, String last_name, String gender, String date, String email,
                String phone, String password, String role, String address, String bio, String status, String avatar) {
        User userInit = null;
        try {
            userInit = User.builder()
                    .username("test_long")
                    .first_name("Hoang")
                    .last_name("Long")
                    .gender("Male")
                    .dob(dateFormat.parse("2001-11-21"))
                    .email("test_long@gmail.com")
                    .phone("0352269303")
                    .password("123456")
                    .role("ROLE_LEARNER")
                    .address("HN")
                    .bio("Swag")
                    .status("active")
                    .avatar("avatar.jpg")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        userRepository.save(userInit);

        User user = null;
        try {
            user = userRepository.findUserByUsername(username);
            if (user == null) {
                throw new ResourceNotFroundException("Studyset not exist with username:" + username);
            }
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setBio(bio);
        try {
            user.setDob(dateFormat.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.setAvatar(avatar);
        user.setAddress(address);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setGender(gender);
        user.setPhone(phone);
        user.setGender(gender);
        user.setStatus(status);
        user.setRole(role);

        User updateUser = userRepository.save(user);

        Assertions.assertThat(username).isEqualTo(updateUser.getUsername());
        Assertions.assertThat(first_name).isEqualTo(updateUser.getFirst_name());
        Assertions.assertThat(last_name).isEqualTo(updateUser.getLast_name());
        Assertions.assertThat(gender).isEqualTo(updateUser.getGender());
        try {
            Assertions.assertThat(dateFormat.parse(date)).isEqualTo(updateUser.getDob());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThat(email).isEqualTo(updateUser.getEmail());
        Assertions.assertThat(phone).isEqualTo(updateUser.getPhone());
        Assertions.assertThat(passwordEncoder.encode(password)).isEqualTo(updateUser.getPassword());
        Assertions.assertThat(role).isEqualTo(updateUser.getRole());
        Assertions.assertThat(address).isEqualTo(updateUser.getAddress());
        Assertions.assertThat(bio).isEqualTo(updateUser.getBio());
        Assertions.assertThat(status).isEqualTo(updateUser.getStatus());
        Assertions.assertThat(avatar).isEqualTo(updateUser.getAvatar());
    }


    @Order(10)
    @ParameterizedTest(name = "{index} => username = {0}, expected={1}")
    @CsvSource({
            "test_long, true",
            "test_long2, false",
    })
    public void testDeleteUser(String username, boolean expected) {
        User userInit = User.builder()
                    .username("test_long")
                    .email("test_long@gmail.com")
                    .build();
        userRepository.save(userInit);

        User user = userRepository.findUserByUsername(username);
        Assertions.assertThat(user!=null).isEqualTo(expected);
        if(user!=null) {
            userRepository.delete(user);
        }
        User user1 = userRepository.findUserByUsername(username);
        Assertions.assertThat(user1).isNull();
    }

    @Order(10)
    @Test
    public void testExistsByToken() {
        User userInit = User.builder()
                .username("test_long")
                .email("test_long@gmail.com")
                .token("12345")
                .build();
        userRepository.save(userInit);

        boolean exists = userRepository.existsByToken("12345");
        Assertions.assertThat(exists).isTrue();

        exists = userRepository.existsByToken("00000");
        Assertions.assertThat(exists).isFalse();
    }

    @Order(10)
    @Test
    public void testFindUserByToken() {
        User userInit = User.builder()
                .username("test_long")
                .email("test_long@gmail.com")
                .token("12345")
                .build();
        userRepository.save(userInit);

        User user = userRepository.findUserByToken("12345");
        Assertions.assertThat(user).isNotNull();

        user = userRepository.findUserByToken("00000");
        Assertions.assertThat(user).isNull();
    }

}
