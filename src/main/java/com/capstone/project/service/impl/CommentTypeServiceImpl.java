package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.CommentType;
import com.capstone.project.repository.CommentTypeRepository;
import com.capstone.project.service.CommentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentTypeServiceImpl implements CommentTypeService {

    private final CommentTypeRepository commentTypeRepository;

    @Autowired
    public CommentTypeServiceImpl(CommentTypeRepository commentTypeRepository) {
        this.commentTypeRepository = commentTypeRepository;
    }

    @Override
    public CommentType getCommentTypeById(int id) {
        CommentType commentType= null;
        try {
            commentType = commentTypeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFroundException("CommentType not exist with id: " + id));
        } catch (ResourceNotFroundException e) {
            e.printStackTrace();
        }
        return commentType;
    }

}
