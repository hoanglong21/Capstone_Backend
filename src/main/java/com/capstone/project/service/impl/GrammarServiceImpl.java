package com.capstone.project.service.impl;

import com.capstone.project.model.Grammar;
import com.capstone.project.service.GrammarService;
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
public class GrammarServiceImpl implements GrammarService {
    private List<Grammar> grammarList = new ArrayList<>();

    public List<Grammar> getGrammarList() {
        return grammarList;
    }

    public List<Grammar> initGrammars() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("Lgrammar.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // Add this line to increase the entity expansion limit
            System.setProperty("entityExpansionLimit", "1000000");

            DocumentBuilder dBuilder = dbf.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("grammar");

            for(int i = 0; i < nodeList.getLength(); i++) {
                Grammar grammar = new Grammar();
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element grammar_point = (Element) node;
                    NodeList grammarTitle = grammar_point.getElementsByTagName("title");
                    if(grammarTitle.getLength() > 0) {
                        grammar.setTitle(grammarTitle.item(0).getTextContent());
                    }
                    NodeList grammarExplanation = grammar_point.getElementsByTagName("explanation");
                    if(grammarExplanation.getLength() > 0) {
                        grammar.setExplanation(grammarExplanation.item(0).getTextContent());
                    }
                    NodeList grammarStructure = grammar_point.getElementsByTagName("structure");
                    if(grammarStructure.getLength() > 0) {
                        grammar.setStructure(grammarStructure.item(0).getTextContent());
                    }
                    NodeList grammarAttention = grammar_point.getElementsByTagName("attention");
                    if(grammarAttention.getLength() > 0) {
                        grammar.setAttention(grammarAttention.item(0).getTextContent());
                    }
                    NodeList grammarAbout = grammar_point.getElementsByTagName("about");
                    if(grammarAbout.getLength() > 0) {
                        grammar.setAbout(grammarAbout.item(0).getTextContent());
                    }
                    NodeList grammarLevel = grammar_point.getElementsByTagName("level");
                    if(grammarLevel.getLength() > 0) {
                        grammar.setLevel(grammarLevel.item(0).getTextContent());
                    }
                    List<String> exampleList = new ArrayList<>();
                    NodeList grammarExample = grammar_point.getElementsByTagName("example");
                    for(int j = 0; j < grammarExample.getLength(); j++) {
                        Element exampleNode = (Element) grammarExample.item(j);
                        exampleList.add(exampleNode.getElementsByTagName("japanese").item(0).getTextContent()+"\n"+
                                exampleNode.getElementsByTagName("english").item(0).getTextContent());
                    }
                    grammar.setExample(exampleList);
                    // new
                    List<Map<String, String>> synonymsList = new ArrayList<>();
                    NodeList synonyms = grammar_point.getElementsByTagName("synonym");
                    for(int j = 0; j < synonyms.getLength(); j++) {
                        Element synonymNode = (Element) synonyms.item(j);
                        Map<String, String> sysMap = new HashMap<>();
                        sysMap.put("text", synonymNode.getElementsByTagName("text").item(0).getTextContent());
                        sysMap.put("explanation", synonymNode.getElementsByTagName("explanation").item(0).getTextContent());
                        synonymsList.add(sysMap);
                    }
                    grammar.setSynonyms(synonymsList);

                    List<Map<String, String>> antonymsList = new ArrayList<>();
                    NodeList antonyms = grammar_point.getElementsByTagName("antonym");
                    for(int j = 0; j < antonyms.getLength(); j++) {
                        Element antonymNode = (Element) antonyms.item(j);
                        Map<String, String> antoMap = new HashMap<>();
                        antoMap.put("text", antonymNode.getElementsByTagName("text").item(0).getTextContent());
                        antoMap.put("explanation", antonymNode.getElementsByTagName("explanation").item(0).getTextContent());
                        antonymsList.add(antoMap);
                    }
                    grammar.setAntonyms(antonymsList);

                    List<Map<String, String>> detailsList = new ArrayList<>();
                    NodeList details = grammar_point.getElementsByTagName("detail");
                    for(int j = 0; j < details.getLength(); j++) {
                        Element antonymNode = (Element) details.item(j);
                        Map<String, String> detailMap = new HashMap<>();
                        detailMap.put("heading", antonymNode.getElementsByTagName("heading").item(0).getTextContent());
                        detailMap.put("text", antonymNode.getElementsByTagName("text").item(0).getTextContent());
                        detailsList.add(detailMap);
                    }
                    grammar.setDetails(detailsList);
                    // end of new
                    grammarList.add(grammar);
                }
            }

            return grammarList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> searchAndPaginate(String query, int level, int page, int pageSize) {
        List<Grammar> results = new ArrayList<>();

        // Perform search based on the query
        for (Grammar grammar : grammarList) {
            if (matchesQuery(grammar, level, query)) {
                results.add(grammar);
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

    private boolean matchesQuery(Grammar grammar, int level, String query) {
        if(level!=0 && !(grammar.getLevel().contains(String.valueOf(level)))) {
            return false;
        }

        if(query==null || query.equals("")) {
            return true;
        }

        String lowercaseQuery = query.toLowerCase();

        // Check if the query matches any property of the Kanji object
        if (searchWithParenthesis(lowercaseQuery, grammar.getTitle().toLowerCase())
                && (level ==0 || grammar.getLevel().contains(String.valueOf(level)))) {
            return true;
        }

        return false;
    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    public boolean searchWithParenthesis(String userInput, String target) {
        return target.contains(userInput) || target.replaceAll("\\([^()]*\\)", "").contains(userInput) ||
                target.replace("(", "").replace(")", "").contains(userInput);
    }
}
