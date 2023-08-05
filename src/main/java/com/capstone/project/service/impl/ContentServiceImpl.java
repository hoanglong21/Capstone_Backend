package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Content;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public List<Content> getAllByCardId(int id) {
        return contentRepository.getContentByCardId(id);
    }

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public Content getContentById(int id) throws ResourceNotFroundException {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Content not exist with id: " + id));
        return content;
    }

    @Override
    public Content updateContent(int id, Content contentDetails) throws ResourceNotFroundException {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Content not exist with id: " + id));
        content.setContent(contentDetails.getContent());
        Content updateContent = contentRepository.save(content);
        return updateContent;
    }

    @Override
    public Boolean deleteContent(int id) throws ResourceNotFroundException {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Content not exist with id: " + id));
        contentRepository.delete(content);
        return true;
    }
}
