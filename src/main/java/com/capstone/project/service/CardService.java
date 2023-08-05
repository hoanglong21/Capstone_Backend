package com.capstone.project.service;

import com.capstone.project.dto.CardWrapper;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;

import java.util.List;
import java.util.Map;

public interface CardService {
    List<Card> getAllCards();

    List<Card> getAllByStudySetId(int id);

    Card createCard(Card card);

    Card getCardById(int id) throws ResourceNotFroundException;

    Card updateCard (int id, Card cardDetails) throws ResourceNotFroundException;

    Boolean deleteCard (int id) throws ResourceNotFroundException;

    Boolean checkBlank(int id) throws ResourceNotFroundException;

    Map<String, Object> getFilterCard(int studySetId, int userId, String[] status, boolean star,
                                      String sortBy, String direction, int page, int size);

}
