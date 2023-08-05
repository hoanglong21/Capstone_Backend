package com.capstone.project.service.impl;

import com.capstone.project.dto.CardWrapper;
import com.capstone.project.dto.StudySetResponse;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.*;
import com.capstone.project.repository.*;
import com.capstone.project.service.CardService;
import com.capstone.project.service.StudySetService;
import com.capstone.project.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudySetServiceImpl implements StudySetService {

    private final StudySetRepository studySetRepository;
    private final CardRepository cardRepository;
    private final ContentRepository contentRepository;

    private final ProgressRepository progressRepository;
    private final CardService cardService;
    private final UserService userService;

    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    @Autowired
    public StudySetServiceImpl(StudySetRepository studySetRepository, CardRepository cardRepository, ContentRepository contentRepository, CardService cardService, UserRepository userRepository, UserService userService, ProgressRepository progressRepository) {
        this.studySetRepository = studySetRepository;
        this.cardRepository = cardRepository;
        this.contentRepository = contentRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.progressRepository = progressRepository;
    }

    @Override
    public List<StudySet> getAllStudySets() {
        return studySetRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public StudySet createStudySet(StudySet studySet) {
        // new
        studySet.setCreated_date(new Date());
        // end of new
        return studySetRepository.save(studySet);
    }

    @Override
    public StudySet getStudySetById(int id) throws ResourceNotFroundException {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Studyset not exist with id: " + id));
        return studySet;
    }

    @Override
    public StudySet updateStudySet(int id, StudySet studySetDetails) throws ResourceNotFroundException {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Studyset not exist with id: " + id));
        studySet.setTitle(studySetDetails.getTitle());
        studySet.setDescription(studySetDetails.getDescription());
        studySet.set_deleted(studySetDetails.is_deleted());
        studySet.set_public(studySetDetails.is_public());
        studySet.set_draft(studySetDetails.is_draft());
        studySet.setDeleted_date(studySetDetails.getDeleted_date());

        StudySet updateStudySet = studySetRepository.save(studySet);
        return updateStudySet;
    }

    @Override
    public Boolean deleteStudySet(int id) throws ResourceNotFroundException {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Studyset not exist with id: " + id));
        studySet.set_deleted(true);
        studySet.setDeleted_date(new Date());
        studySetRepository.save(studySet);
        return true;
    }

    @Override
    public Boolean deleteHardStudySet(int id) throws ResourceNotFroundException {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Studyset not exist with id: " + id));

        // delete all the cards and contents associated with the study set
        for (Card card : cardRepository.getCardByStudySetId(studySet.getId())) {
            for (Content content : contentRepository.getContentByCardId(card.getId())) {
                contentRepository.delete(content);
            }
            cardRepository.delete(card);
        }
        studySetRepository.delete(studySet);
        return true;
    }

    @Override
    public List<Integer> checkBlankCard(int id) throws ResourceNotFroundException {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Studyset not exist with id: " + id));
        List<Integer> listCardIds = new ArrayList<>();
        for (Card card : cardRepository.getCardByStudySetId(studySet.getId())) {
            if(cardService.checkBlank(card.getId())) {
                listCardIds.add(card.getId());
            }
        }
        return listCardIds;
    }

    @Override
    public List<StudySet> getAllStudySetByUser(String username) throws ResourceNotFroundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFroundException("Studyset not exist with author: " + username);
        }
        List<StudySet> studySets = studySetRepository.findStudySetByAuthor_id(user.getId());
        return studySets;
    }

    @Override
    public Map<String, Object> getFilterList(Boolean isDeleted, Boolean isPublic, Boolean isDraft, String search, int type, int authorId, String authorName,
                                             String fromDeleted, String toDeleted, String fromCreated, String toCreated,
                                             String sortBy, String direction, int page, int size) {
        int offset = (page - 1) * size;

        String query = "SELECT s.id, s.title, s.description, s.is_deleted, s.is_public, s.is_draft, s.type_id, s.author_id, s.deleted_date, s.created_date," +
                " u.username as author, u.avatar, u.first_name AS author_firstname, u.last_name AS author_lastname," +
                " (SELECT COUNT(*) FROM capstone.card WHERE studyset_id = s.id) AS `count` FROM studyset s LEFT JOIN `user` u ON s.author_id = u.id WHERE 1=1";

        Map<String, Object> parameters = new HashMap<>();

        if (isDeleted != null) {
            query += " AND s.is_deleted = :isDeleted";
            parameters.put("isDeleted", isDeleted);
        }

        if (isPublic != null) {
            query += " AND s.is_public = :isPublic";
            parameters.put("isPublic", isPublic);
        }

        if (isDraft != null) {
            query += " AND s.is_draft = :isDraft";
            parameters.put("isDraft", isDraft);
        }

        if (search != null && !search.isEmpty()) {
            query += " AND (s.title LIKE :search OR s.description LIKE :search)";
            parameters.put("search", "%" + search + "%");
        }

        if ((isDeleted == null || isDeleted)) {
            if (fromDeleted != null) {
                query += " AND DATE(s.deleted_date) >= :fromDeleted";
                parameters.put("fromDeleted", fromDeleted);
            }
            if (toDeleted != null) {
                query += " AND DATE(s.deleted_date) <= :toDeleted";
                parameters.put("toDeleted", toDeleted);
            }
        }

        if (authorId != 0) {
            query += " AND s.author_id = :authorId";
            parameters.put("authorId", authorId);
        }

        if (authorId == 0 && authorName != null && !authorName.isEmpty()) {
            query += " AND (u.username LIKE :name OR u.first_name LIKE :name OR u.last_name LIKE :name OR CONCAT(u.first_name, ' ', u.last_name))";
            parameters.put("name", "%" + authorName + "%");
        }

        if (type != 0) {
            query += " AND s.type_id = :typeId";
            parameters.put("typeId", type);
        }

        if (fromCreated != null && !fromCreated.equals("")) {
            query += " AND DATE(s.created_date) >= :fromCreated";
            parameters.put("fromCreated", fromCreated);
        }
        if (toCreated != null && !toCreated.equals("")) {
            query += " AND DATE(s.created_date) <= :toCreated";
            parameters.put("toCreated", toCreated);
        }

        if(sortBy != null && !sortBy.equals("") && direction != null && !direction.equals("")) {
            query += " ORDER BY " + sortBy + " " + direction;
        }

        Query q = em.createNativeQuery(query, "StudySetResponseCustomListMapping");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<StudySetResponse> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;
    }

    @Override
    public List<Map<String, Object>> getQuizByStudySetId(int studySetId, int[] questionType, int numberOfQuestion, int userId, boolean star) throws Exception {
        if(numberOfQuestion<=0) {
            throw new Exception("Number of question must at least 1");
        }
        if(questionType.length==0) {
            throw new Exception("Must have at least 1 type");
        }
        List<Card> cardList = cardRepository.getCardByStudySetId(studySetId);
        return getLearningMethod(cardList, questionType, numberOfQuestion, true, userId, star);
    }

    @Override
    public List<Map<String, Object>> getLearningStudySetId(int userId, int studySetId, int[] questionType, String[] progressType, boolean isRandom, boolean star) throws Exception {
        if(questionType.length==0) {
            throw new Exception("Must have at least 1 type");
        }
        if(progressType.length==0) {
            throw new Exception("Must have at least 1 type");
        }
        List<Card> cardList = cardRepository.findCardByProgress(userId, progressType, studySetId);
        return getLearningMethod(cardList, questionType, cardList.size(), isRandom, userId, star);
    }

    @Override
    public Map<String, Integer> countCardInSet(int studySetId, int userId) {
        Map<String, Integer> result = new HashMap<>();
        result.put("Not studied", studySetRepository.countCardInSetWithCondition(studySetId, userId, "not studied", false));
        result.put("Still learning", studySetRepository.countCardInSetWithCondition(studySetId, userId, "still learning", false));
        result.put("Mastered", studySetRepository.countCardInSetWithCondition(studySetId, userId, "mastered", false));
        result.put("Not studied star", studySetRepository.countCardInSetWithCondition(studySetId, userId, "not studied", true));
        result.put("Still learning star", studySetRepository.countCardInSetWithCondition(studySetId, userId, "still learning", true));
        result.put("Mastered star", studySetRepository.countCardInSetWithCondition(studySetId, userId, "mastered", true));
        return result;
    }

    private List<Map<String, Object>> getLearningMethod(List<Card> cardList, int[] questionType, int numberOfQuestion, boolean isRandom, int userId, boolean star) {
        // Shuffle the cardList to randomize the order of cards
        if(isRandom) {
            Collections.shuffle(cardList);
        }
        List<CardWrapper> initCardWrappers = new ArrayList<>();

        for (Card card : cardList) {
            List<Content> contents = contentRepository.getContentByCardId(card.getId());
            Progress progress = progressRepository.findByCardIdAndUserId(userId, card.getId());
            CardWrapper cardWrapper = new CardWrapper(card, contents, progress);
            initCardWrappers.add(cardWrapper);
        }

        int cardsPerType = numberOfQuestion / questionType.length;

//        List<CardWrapper> questionCardSet = initCardWrappers.subList(0, Math.min(numberOfQuestion, initCardWrappers.size()));
        List<CardWrapper> questionCardSet = new ArrayList<>();
        for(int i=0; i<initCardWrappers.size(); i++) {
            if(!star || (star && initCardWrappers.get(i).getProgress().is_star())){
                questionCardSet.add(initCardWrappers.get(i));
            }
            if(questionCardSet.size()==numberOfQuestion) {
                break;
            }
        }

        List<CardWrapper> cardListCloneForAnswers = new ArrayList<>(initCardWrappers);
        List<Map<String, Object>> response = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < questionCardSet.size(); i++) {
            int currentType;
            if(i<cardsPerType) {
                currentType = questionType[0];
            } else if(i<cardsPerType*2 && questionType.length>=2) {
                currentType = questionType[1];
            } else { // remain of cardsPerType
                currentType = questionType[questionType.length-1];
            }
            Collections.shuffle(cardListCloneForAnswers);
            List<CardWrapper> answerCardSet = new ArrayList<>();


            if(currentType == 1) {
                answerCardSet.add(questionCardSet.get(i));
            }
            if(currentType == 2) {
                answerCardSet.add(questionCardSet.get(i));
                for (CardWrapper randomCard : cardListCloneForAnswers) {
                    if (answerCardSet.size() == 4) {
                        break;
                    }
                    // Make sure not to add the same cardWrapper as the original one
                    if (!questionCardSet.get(i).equals(randomCard)) {
                        answerCardSet.add(randomCard);
                    }
                }
            }
            if(currentType == 3) {
                int x = rand.nextInt(2); // 0, 1
                if(x==0) {
                    answerCardSet.add(questionCardSet.get(i));
                } else {
                    for (CardWrapper randomCard : cardListCloneForAnswers) {
                        if (!questionCardSet.get(i).equals(randomCard)) {
                            answerCardSet.add(randomCard);
                            break;
                        }
                    }
                }
            }
            Collections.shuffle(answerCardSet);

            Map<String, Object> map = new HashMap<>();
            map.put("question", questionCardSet.get(i));
            map.put("question_type", currentType);
            map.put("answers", answerCardSet);
            response.add(map);
        }

        return response;
    }

}
