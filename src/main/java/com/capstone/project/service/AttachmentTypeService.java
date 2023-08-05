package com.capstone.project.service;

import com.capstone.project.model.Attachment;
import com.capstone.project.model.AttachmentType;
import com.capstone.project.model.FeedbackType;

import java.util.List;

public interface AttachmentTypeService {
    AttachmentType getAttachmentTypeById (int id);

    List<AttachmentType> getAllType();
}
