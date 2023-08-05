package com.capstone.project.repository;

import com.capstone.project.model.Attachment;
import com.capstone.project.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {

    List<Attachment> getAttachmentBySubmissionId(int id);
    List<Attachment> getAttachmentByAssignmentId(int id);
    List<Attachment> getAttachmentByPostId(int id);
}
