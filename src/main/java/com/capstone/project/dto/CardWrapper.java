package com.capstone.project.dto;

import com.capstone.project.model.Card;
import com.capstone.project.model.Content;
import com.capstone.project.model.Progress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardWrapper {
    private Card card;
    private List<Content> content;
    private Progress progress;
}
