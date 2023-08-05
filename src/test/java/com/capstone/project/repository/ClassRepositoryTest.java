package com.capstone.project.repository;
import com.capstone.project.model.Class;
import com.capstone.project.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => classcode={0}," +
            " find = {1}, expected={2}")
    @CsvSource({
            "G6tE5q, G6tE5q, true",
            "N7h9IK, 6yTr4E, false",
    })
    public void testFindByClasscode(String classcode,String find, boolean expected){
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder()
                .user(user)
                .classcode(classcode)
                .build();
        classRepository.save(classroom);

        Class findClass = classRepository.findByClasscode(find);
         assertThat(findClass != null).isEqualTo(expected);
    }
}
