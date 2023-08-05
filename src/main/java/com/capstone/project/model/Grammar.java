package com.capstone.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grammar {
    private String title;
    private String explanation;
    private String structure;
    private String attention;
    private String about;
    private String level;
    private List<String> example;
    private List<Map<String, String>> synonyms;
    private List<Map<String, String>> antonyms;
    private List<Map<String, String>> details;
}
