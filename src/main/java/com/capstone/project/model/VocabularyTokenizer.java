package com.capstone.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabularyTokenizer {
    private String word;
    private List<String> partOfSpeech;
    private String dictionaryForm;
}
