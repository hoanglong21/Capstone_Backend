package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.AttachmentRepository;
import com.capstone.project.service.impl.AttachmentSerivceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentSerivceImpl attachmentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Order(1)
    @Test
    void testGetAllAttachment() {
        Attachment attachment = Attachment.builder()
                .file_url("homework.doc")
                .build();
        List<Attachment> attachments = List.of(attachment);
        when(attachmentRepository.findAll()).thenReturn(attachments);
        assertThat(attachmentServiceImpl.getAllAttachment().size()).isGreaterThan(0);
    }

    @Order(2)
    @Test
    void testGetAttachmentBySubmissionId(){
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment1 = Attachment.builder().file_url("homework1.doc").build();
        Attachment attachment2 = Attachment.builder().file_url("homework2.doc").build();
        attachments.add(attachment1);
        attachments.add(attachment2);
        when(attachmentRepository.getAttachmentBySubmissionId(any(Integer.class))).thenReturn(attachments);
        List<Attachment> retrievedAttachments = attachmentServiceImpl.getAllAttachmentBySubmissionId(1);
        assertThat(retrievedAttachments).isEqualTo(attachments);
    }

    @Order(3)
    @Test
    void testGetAttachmentByAssignmentId(){
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment1 = Attachment.builder().file_url("homework1.doc").build();
        Attachment attachment2 = Attachment.builder().file_url("homework2.doc").build();
        attachments.add(attachment1);
        attachments.add(attachment2);
        when(attachmentRepository.getAttachmentByAssignmentId(any(Integer.class))).thenReturn(attachments);
        List<Attachment> retrievedAttachments = attachmentServiceImpl.getAllAttachmentByAssignmentId(1);
        assertThat(retrievedAttachments).isEqualTo(attachments);
    }

    @Order(4)
    @Test
    void testGetAttachmentByPostId(){
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment1 = Attachment.builder().file_url("homework1.doc").build();
        Attachment attachment2 = Attachment.builder().file_url("homework2.doc").build();
        attachments.add(attachment1);
        attachments.add(attachment2);
        when(attachmentRepository.getAttachmentByPostId(any(Integer.class))).thenReturn(attachments);
        List<Attachment> retrievedAttachments = attachmentServiceImpl.getAllAttachmentByPostId(1);
        assertThat(retrievedAttachments).isEqualTo(attachments);
    }

    @Order(5)
    @ParameterizedTest(name = "index => assignmentId={0}, typeId={1},submissionId{2},file{3}")
    @CsvSource({
            "1,2,1,On thi N3 ",
            "1,2,2,On thi N2 "
    })
    public void testCreateAttachment(int assignmentId, int typeId, int submissionId,String file) {

        Attachment attachment = Attachment.builder()
                .assignment(Assignment.builder().id(assignmentId).build())
                .attachmentType(AttachmentType.builder().id(typeId).build())
                .submission(Submission.builder().id(submissionId).build())
                .file_name(file)
                .build();
        when(attachmentRepository.save(any())).thenReturn(attachment);

        Attachment createdattachment = attachmentServiceImpl.createAttachment(attachment);
        assertThat(attachment).isEqualTo(createdattachment);
    }

    @Order(6)
    @Test
    public void testCreateAttachments() {

        List<Attachment> attachmentList = new ArrayList<>();
        Attachment attachment1 =  Attachment.builder().assignment(Assignment.builder().id(1).build())
                .attachmentType(AttachmentType.builder().id(1).build())
                .submission(Submission.builder().id(1).build())
                .file_name("guide")
                .build();
        Attachment attachment2 =  Attachment.builder().assignment(Assignment.builder().id(2).build())
                .attachmentType(AttachmentType.builder().id(2).build())
                .submission(Submission.builder().id(2).build())
                .file_name("guide")
                .build();

        attachmentList.add(attachment1);
        attachmentList.add(attachment2);
    }

    @Order(7)
    @Test
    void testGetAttachmentById(){
        Attachment attachment = Attachment.builder()
                .file_url("haha.doc")
                .build();
        when(attachmentRepository.findById(any())).thenReturn(Optional.ofNullable(attachment));
        try{
            Attachment getAttachment = attachmentServiceImpl.getAttachmentById(1);
            assertThat(getAttachment).isEqualTo(attachment);
        }catch (ResourceNotFroundException e){
            e.printStackTrace();
        }
    }


    @Order(8)
    @ParameterizedTest(name = "index => assignmentId={0}, typeId={1},submissionId{2},file{3}")
    @CsvSource({
            "1,2,1,On thi N3 ",
            "1,2,2,On thi N2 "
    })
    public void testUpdateAttachment(int assignmentId, int typeId, int submissionId,String file) {
            try{

                Attachment attachment_new = Attachment.builder()
                        .file_url("homework.doc")
                        .build();

                Attachment attachment = Attachment.builder()
                        .assignment(Assignment.builder().id(assignmentId).build())
                        .attachmentType(AttachmentType.builder().id(typeId).build())
                        .submission(Submission.builder().id(submissionId).build())
                        .file_name(file)
                        .build();

                when(attachmentRepository.findById(any())).thenReturn(Optional.ofNullable(attachment_new));
                when(attachmentRepository.save(any())).thenReturn(attachment);

                Attachment created_attachment = attachmentServiceImpl.updateAttachment(1,attachment);
                assertThat(created_attachment).isEqualTo(attachment);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @Order(9)
    @Test
    void testDeleteAttachment() {

        Attachment attachment = Attachment.builder()
                .id(1)
                .assignment(Assignment.builder().id(1).build())
                .attachmentType(AttachmentType.builder().id(1).build())
                .submission(Submission.builder().id(1).build())
                .file_url("home.doc")
                .build();

        when(attachmentRepository.findById(any())).thenReturn(Optional.ofNullable(attachment));
        doNothing().when(attachmentRepository).delete(attachment);
        try {
            attachmentServiceImpl.deleteAttachment(1);
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        verify(attachmentRepository, times(1)).delete(attachment);
    }



    }

