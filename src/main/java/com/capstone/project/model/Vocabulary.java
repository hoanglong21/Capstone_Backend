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
public class Vocabulary {
    private List<String> kanji;
    private List<String> reading;
    private List<Sense> sense;
}
