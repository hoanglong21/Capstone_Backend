package com.capstone.project.dto;

import com.capstone.project.model.Card;
import com.capstone.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressUpdateRequest {
    User user;
    Card card;
    boolean isStar;
    String picture;
    String audio;
    String note;
    String status;
}
