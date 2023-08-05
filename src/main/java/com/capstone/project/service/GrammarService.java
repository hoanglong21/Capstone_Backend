package com.capstone.project.service;

import com.capstone.project.model.Grammar;
import com.capstone.project.model.Kanji;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface GrammarService {

    public List<Grammar> initGrammars();

    public Map<String, Object> searchAndPaginate(String query, int level, int page, int pageSize);

    public List<Grammar> getGrammarList();
}
