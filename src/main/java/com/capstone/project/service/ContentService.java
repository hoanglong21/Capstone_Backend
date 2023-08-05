package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Content;

import java.util.List;

public interface ContentService {

    List<Content> getAllByCardId(int id);

    Content createContent(Content content);

    Content getContentById(int id) throws ResourceNotFroundException;

    Content updateContent(int id, Content contentDetails) throws ResourceNotFroundException;

    Boolean deleteContent(int id) throws ResourceNotFroundException;
}
