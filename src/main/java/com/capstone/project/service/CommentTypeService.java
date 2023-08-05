package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.CommentType;
import com.capstone.project.model.StudySetType;

public interface CommentTypeService {
    CommentType getCommentTypeById(int id);
}
