package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0}, testId={1},studysetId{2},rootId{3}, postId{4}, typeId{5},content{6},created_date{7}")
    @CsvSource({
            "1,1,1,1,1,1,Winter is coming,2023-3-5 ",
            "2,2,2,2,2,2,Summer time, 2023-3-5 "
    })
    public void testComment(int userId, int testId,int studysetId,int rootId,int postId,int typeId,String content, String created_date){
              try{
        Comment comment = Comment.builder()
                       .user(User.builder().id(userId).build())
                       .test(Test.builder().id(testId).build())
                       .studySet(StudySet.builder().id(studysetId).build())
                       .root(Comment.builder().id(rootId).build())
                       .post(Post.builder().id(postId).build())
                       .commentType(CommentType.builder().id(typeId).build())
                       .content(content)
                       .created_date(dateFormat.parse(created_date)).build();
        assertThat(comment).isNotNull();
        assertThat(comment.getUser().getId()).isEqualTo(userId);
        assertThat(comment.getTest().getId()).isEqualTo(testId);
        assertThat(comment.getStudySet().getId()).isEqualTo(studysetId);
        assertThat(comment.getRoot().getId()).isEqualTo(rootId);
        assertThat(comment.getPost().getId()).isEqualTo(postId);
        assertThat(comment.getCommentType().getId()).isEqualTo(typeId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreated_date()).isEqualTo(created_date);
    }catch (ParseException e){
                  throw new RuntimeException(e);
              }
    }
}
