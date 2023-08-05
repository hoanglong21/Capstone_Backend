package com.capstone.project.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AttachmentTest {
    @ParameterizedTest(name = "index => file_name{0},file_type{1},file_url{2}, assignmentId={3}, typeId={4},submissionId{5},postId{3}")
    @CsvSource({
            "homework,doc,homework.doc,1,2,1,1 ",
            "test,pdf,test.pdf,1,2,2,2 "
    })
    public void testAttachment(String file_name,String file_type,String file_url,int assignmentId, int typeId, int submissionId,int postId){
         Attachment attachment = Attachment.builder()
                 .assignment(Assignment.builder().id(assignmentId).build())
                 .file_name(file_name)
                 .file_type(file_type)
                 .file_url(file_url)
                 .attachmentType(AttachmentType.builder().id(typeId).build())
                 .submission(Submission.builder().id(submissionId).build())
                 .post(Post.builder().id(postId).build())
                 .build();
         assertThat(attachment).isNotNull();
         assertThat(attachment.getFile_name()).isEqualTo(file_name);
         assertThat(attachment.getFile_type()).isEqualTo(file_type);
         assertThat(attachment.getFile_url()).isEqualTo(file_url);
         assertThat(attachment.getAssignment().getId()).isEqualTo(assignmentId);
         assertThat(attachment.getAttachmentType().getId()).isEqualTo(typeId);
         assertThat(attachment.getSubmission().getId()).isEqualTo(submissionId);
         assertThat(attachment.getPost().getId()).isEqualTo(postId);
    }
}
