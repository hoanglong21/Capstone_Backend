package com.capstone.project.repository;
import com.capstone.project.model.Class;
import com.capstone.project.model.Comment;
import com.capstone.project.model.Post;
import com.capstone.project.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetPostByClassroomId(Boolean trueId){

        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Post post = Post.builder()
                .user(user)
                .classroom(classroom)
                .content("Submit assignment tomorrow")
                .build();
        postRepository.save(post);

        if(trueId) {
            List<Post> result = postRepository.getPostByClassroomId(classroom.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Post> result = postRepository.getPostByClassroomId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }
}
