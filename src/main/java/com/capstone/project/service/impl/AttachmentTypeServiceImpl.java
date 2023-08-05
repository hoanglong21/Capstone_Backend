package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.AttachmentType;
import com.capstone.project.repository.AttachmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentTypeServiceImpl implements com.capstone.project.service.AttachmentTypeService {

    private final AttachmentTypeRepository attachmentTypeRepository;

    @Autowired
    public AttachmentTypeServiceImpl(AttachmentTypeRepository attachmentTypeRepository) {
        this.attachmentTypeRepository = attachmentTypeRepository;
    }

    @Override
    public AttachmentType getAttachmentTypeById(int id) {
        AttachmentType attachmentType= null;
        try {
            attachmentType = attachmentTypeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("AttachmentType not exist with id: " + id));
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        return attachmentType;
    }

    @Override
    public List<AttachmentType> getAllType() {
        return attachmentTypeRepository.findAll();
    }
}
