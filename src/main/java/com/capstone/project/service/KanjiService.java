package com.capstone.project.service;

import com.capstone.project.model.Kanji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface KanjiService {

    public List<Kanji> initKanji();

    public Map<String, Object> searchAndPaginate(String query, int page, int pageSize);

    public List<Kanji> getKanjiList();
}


