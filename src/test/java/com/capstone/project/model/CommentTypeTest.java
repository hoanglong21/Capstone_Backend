package com.capstone.project.model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentTypeTest {

    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "CommentType1",
            "CommentType2"
    })
    public void testCommentType(String name){
        CommentType commentType = CommentType.builder()
                .name(name).build();
        assertThat(commentType).isNotNull();
        assertThat(commentType.getName()).isEqualTo(name);
    }
}
