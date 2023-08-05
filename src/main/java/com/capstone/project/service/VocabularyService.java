package com.capstone.project.service;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.capstone.project.model.Example;
import com.capstone.project.model.Kanji;
import com.capstone.project.model.Sense;
import com.capstone.project.model.Vocabulary;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface VocabularyService {

    public List<Vocabulary> initVocabulary();

    public Map<String, Object> searchAndPaginate(String query, int page, int pageSize);

    public List<Vocabulary> getVocabularyList();
}
