package com.capstone.project.repository;
import com.capstone.project.model.*;
import com.capstone.project.model.Class;
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
public class CommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StudySetRepository studySetRepository;

    @Order(1)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetCommentByTestId(Boolean trueId){
        User user = User.builder().username("test_stub").email("teststub@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Test test = Test.builder().description("Test for all").classroom(classroom).duration(12).title("Progress test").user(user).build();
        testRepository.save(test);

        Comment comment = Comment.builder()
                .user(user)
                .test(test)
                .commentType(CommentType.builder().id(1).build())
                .build();
        commentRepository.save(comment);

        if(trueId) {
            List<Comment> result = commentRepository.getCommentByTestId(test.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Comment> result = commentRepository.getCommentByTestId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Order(2)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetCommentByPostId(Boolean trueId){

        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Class classroom = Class.builder().class_name("Luyen thi N3").description("On thi N3").user(user).build();
        classRepository.save(classroom);

        Post post = Post.builder().user(user).classroom(classroom).content("Submit assignment tomorrow").build();
        postRepository.save(post);

        Comment comment = Comment.builder().user(user).post(post).commentType(CommentType.builder().id(1).build())
                .build();
        commentRepository.save(comment);
        if(trueId) {
            List<Comment> result = commentRepository.getCommentByPostId(post.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Comment> result = commentRepository.getCommentByPostId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Order(3)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetCommentByStudySetId(Boolean trueId){
        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        StudySet studySet = StudySet.builder().title("Stuff").studySetType(StudySetType.builder().id(1).build()).
                user(user).build();
        studySetRepository.save(studySet);

        Comment comment = Comment.builder().user(user).studySet(studySet).commentType(CommentType.builder().id(1).build())
                .build();
        commentRepository.save(comment);
        if(trueId) {
            List<Comment> result = commentRepository.getCommentByStudySetId(studySet.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Comment> result = commentRepository.getCommentByStudySetId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

    @Order(4)
    @ParameterizedTest(name = "{index} => trueId={0}")
    @CsvSource({
            "true", "false"
    })
    public void testGetCommentByRootId(Boolean trueId){

        User user = User.builder().username("test_stuff").email("teststuff@gmail.com").build();
        userRepository.save(user);

        Comment rootComment = Comment.builder().user(user).commentType(CommentType.builder().id(1).build()).build();
        commentRepository.save(rootComment);

        Comment childComment = Comment.builder().user(user).commentType(CommentType.builder().id(1).build()).root(rootComment)
                .build();
        commentRepository.save(childComment);
        if(trueId) {
            List<Comment> result = commentRepository.getCommentByRootId(rootComment.getId());
            assertThat(result.size()).isGreaterThan(0);
        } else {
            List<Comment> result = commentRepository.getCommentByRootId(-1);
            assertThat(result.size()).isEqualTo(0);
        }
    }

}
