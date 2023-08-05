package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @ParameterizedTest(name = "{index} => username={0}, first_name={1}, last_name={2}, gender={3}, dob={4}, email={5}," +
            " phone={6}, password={7}, role={8}, address={9}, bio={10}, status={11}, avatar={12}")
    @CsvSource({
            "long, Hoang, Long, Male, 2001-11-21, long@gmail.com, 0352269303, 123456, ROLE_LEARNER, HN, Swag, active, avatar.jpg",
            "tuyet, Nguyen, Tuyet, Female, 2001-09-27, tuyet@gmail.com, 0352269304, 123456, ROLE_ADMIN, HG, Hello, pending, avatar2.png"
    })
    public void testUser(String username, String first_name, String last_name, String gender, String date, String email,
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

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getFirst_name()).isEqualTo(first_name);
        assertThat(user.getLast_name()).isEqualTo(last_name);
        assertThat(user.getGender()).isEqualTo(gender);
        assertThat(user.getDob()).isEqualTo(date);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPhone()).isEqualTo(phone);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getRole()).isEqualTo(role);
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getBio()).isEqualTo(bio);
        assertThat(user.getStatus()).isEqualTo(status);
        assertThat(user.getAvatar()).isEqualTo(avatar);
    }
}