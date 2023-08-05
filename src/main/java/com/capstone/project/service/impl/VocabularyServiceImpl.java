package com.capstone.project.service.impl;

import com.capstone.project.model.Example;
import com.capstone.project.model.Sense;
import com.capstone.project.model.Vocabulary;
import com.capstone.project.service.VocabularyService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VocabularyServiceImpl implements VocabularyService {
    private List<Vocabulary> vocabularyList = new ArrayList<>();

    public List<Vocabulary> getVocabularyList() {
        return vocabularyList;
    }

    public List<Vocabulary> initVocabulary() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("OmohaDictionary.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

// Add this line to increase the entity expansion limit
            System.setProperty("entityExpansionLimit", "1000000");

            DocumentBuilder dBuilder = dbf.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("entry");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Vocabulary vocabulary = new Vocabulary();
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element entry = (Element) node;
                    NodeList kanjisList = entry.getElementsByTagName("keb");
                    NodeList readingsList = entry.getElementsByTagName("reb");
                    NodeList sensesList = entry.getElementsByTagName("sense");
                    List<String> kanjis = new ArrayList<>();
                    List<String> readings = new ArrayList<>();
                    List<Sense> senses = new ArrayList<>();
                    for (int j = 0; j < kanjisList.getLength(); j++) {
                        kanjis.add(kanjisList.item(j).getTextContent());
                    }
                    for (int j = 0; j < readingsList.getLength(); j++) {
                        readings.add(readingsList.item(j).getTextContent());
                    }
                    for (int j = 0; j < sensesList.getLength(); j++) {
                        Sense sense = new Sense();
                        Element senseNode = (Element) sensesList.item(j);

                        NodeList typesList = senseNode.getElementsByTagName("pos");
                        List<String> types = new ArrayList<>();
                        for (int k = 0; k < typesList.getLength(); k++) {
                            types.add(typesList.item(k).getTextContent());
                        }
                        sense.setType(types);

                        NodeList relatesList = senseNode.getElementsByTagName("xref");
                        List<String> relates = new ArrayList<>();
                        for (int k = 0; k < relatesList.getLength(); k++) {
                            relates.add(relatesList.item(k).getTextContent());
                        }
                        sense.setRelate(relates);

                        NodeList definitionsList = senseNode.getElementsByTagName("gloss");
                        List<String> definitions = new ArrayList<>();
                        for (int k = 0; k < definitionsList.getLength(); k++) {
                            definitions.add(definitionsList.item(k).getTextContent());
                        }
                        sense.setDefinition(definitions);

                        NodeList examplesList = senseNode.getElementsByTagName("example");
                        List<Example> examples = new ArrayList<>();
                        for (int k = 0; k < examplesList.getLength(); k++) {
                            Example example = new Example();
                            Element exampleNode = (Element) examplesList.item(k);

                            example.setExampleText(exampleNode.getElementsByTagName("ex_text").item(0).getTextContent());
                            example.setExampleSentenceJapanese(exampleNode.getElementsByTagName("ex_sent").item(0).getTextContent());
                            example.setExampleSentenceVietnamese(exampleNode.getElementsByTagName("ex_sent").item(1).getTextContent());
                            examples.add(example);
                        }
                        sense.setExample(examples);

                        senses.add(sense);
                    }
                    vocabulary.setKanji(kanjis);
                    vocabulary.setReading(readings);
                    vocabulary.setSense(senses);

                    vocabularyList.add(vocabulary);
                }
            }

            return vocabularyList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> searchAndPaginate(String query, int page, int pageSize) {
        List<Vocabulary> results = new ArrayList<>();

        // Perform search based on the query
        for (Vocabulary vocabulary : vocabularyList) {
            if (matchesQuery(vocabulary, query)) {
                results.add(vocabulary);
            }
        }

        // Apply pagination
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, results.size());

        Map<String, Object> response = new HashMap<>();
        response.put("list", results.subList(startIndex, endIndex));
        response.put("currentPage", page);
        response.put("totalItems", results.size());
        response.put("totalPages", (int) Math.ceil((double) results.size() / pageSize));

        return response;
    }

    private boolean matchesQuery(Vocabulary vocabulary, String query) {
        if(query==null || query.equals("")) {
            return true;
        }
        String lowercaseQuery = query.toLowerCase();

        // Check if the query matches any property of the Kanji object
        for (String kanji : vocabulary.getKanji()) {
            if (kanji.toLowerCase().contains(lowercaseQuery)) {
                return true;
            }
        }

        for (String reading : vocabulary.getReading()) {
            if (reading.toLowerCase().contains(lowercaseQuery)) {
                return true;
            }
        }

        for (Sense sense : vocabulary.getSense()) {
            for(String definition : sense.getDefinition()) {
                //ignore accented Vietnamese
                if (normalizeString(definition).contains(normalizeString(query))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}
