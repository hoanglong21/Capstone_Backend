package com.capstone.project.service.impl;

import com.capstone.project.dto.CardWrapper;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.model.Content;
import com.capstone.project.repository.CardRepository;
import com.capstone.project.repository.ContentRepository;
import com.capstone.project.repository.ProgressRepository;
import com.capstone.project.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final ContentRepository contentRepository;
    private final ProgressRepository progressRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, ContentRepository contentRepository, ProgressRepository progressRepository) {
        this.cardRepository = cardRepository;
        this.contentRepository = contentRepository;
        this.progressRepository = progressRepository;
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<Card> getAllByStudySetId(int id) {
        return cardRepository.getCardByStudySetId(id);
    }

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card getCardById(int id) throws ResourceNotFroundException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Card not exist with id: " + id));
        return card;
    }

    @Override
    public Card updateCard(int id, Card cardDetails) throws ResourceNotFroundException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Card not exist with id: " + id));
        card.setPicture(cardDetails.getPicture());
        card.setAudio(cardDetails.getAudio());

        Card updateCard = cardRepository.save(card);
        return updateCard;
    }

    @Override
    public Boolean deleteCard(int id) throws ResourceNotFroundException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Card not exist with id: " + id));
        for (Content content: contentRepository.getContentByCardId(card.getId())) {
            contentRepository.delete(content);
        }
        cardRepository.delete(card);
        return true;
    }

    @Override
    public Boolean checkBlank(int id) throws ResourceNotFroundException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Card not exist with id: " + id));
        int count = 0;
        if ((card.getAudio() == null||card.getAudio().equals(""))  && (card.getPicture() == null||card.getPicture().equals(""))) {
            List<Content> contents = contentRepository.getContentByCardId(card.getId());
            for (Content content : contents) {
                if (content.getContent()==null||content.getContent().equals("")) {
                    count += 1;
                }
            }
            if(count==contents.size()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> getFilterCard(int studySetId, int userId, String[] status, boolean star,
                                             String sortBy, String direction, int page, int size) {
        if(sortBy=="star") {
            sortBy = "p.is_star";
        } else {
            sortBy = "p.status";
        }
        if(direction!="ASC") {
            direction="DESC";
        }
        List<Card> cardList = cardRepository.getCardInSetWithCondition(studySetId, userId, status, star, sortBy, direction);

        int totalItems = cardList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Calculate the start and end index for the requested page
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalItems);

        List<CardWrapper> cardWrappers = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            Card card = cardList.get(i);
            CardWrapper responseCard = new CardWrapper();
            responseCard.setCard(card);
            responseCard.setContent(contentRepository.getContentByCardId(card.getId()));
            responseCard.setProgress(progressRepository.findByCardIdAndUserId(userId, card.getId()));
            cardWrappers.add(responseCard);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("list", cardWrappers);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

}
