package com.capstone.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Example {
    private String exampleText;
    private String exampleSentenceJapanese;
    private String exampleSentenceVietnamese;
}
