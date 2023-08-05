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
public class PostTest {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @ParameterizedTest(name = "index => userId={0}, classId={1}, content{2}, created_date{3}, modified_date{4}")
    @CsvSource({
            "1,1,Submit all assignment,2023-8-9, 2023-8-14 ",
            "2,2, Class will be off on Sunday, 2023-8-9, 2023-8-15 "
    })
    public void testPost(int userId,int classId,String content,String createdDate,String modifiedDate ) throws ParseException {
        try {
            Post post = Post.builder()
                    .user(User.builder().id(userId).build())
                    .classroom(Class.builder().id(classId).build())
                    .content(content)
                    .created_date(dateFormat.parse(createdDate))
                    .modified_date(dateFormat.parse(modifiedDate))
                    .build();
            assertThat(post).isNotNull();
            assertThat(post.getUser().getId()).isEqualTo(userId);
            assertThat(post.getClassroom().getId()).isEqualTo(classId);
            assertThat(post.getContent()).isEqualTo(content);
            assertThat(post.getCreated_date()).isEqualTo(createdDate);
            assertThat(post.getModified_date()).isEqualTo(modifiedDate);
        }catch (ParseException e){
            throw new RuntimeException();
        }
    }
}
