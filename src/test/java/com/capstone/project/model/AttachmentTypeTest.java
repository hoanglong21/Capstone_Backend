package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttachmentTypeTest {
    @ParameterizedTest(name = "index => name={0}")
    @CsvSource({
            "AttachmentType1",
            "AttachmentType2"
    })
    public void testAttachmentType(String name){
        AttachmentType attachmentType = AttachmentType.builder()
                .name(name).build();
        assertThat(attachmentType).isNotNull();
        assertThat(attachmentType.getName()).isEqualTo(name);
    }
}
