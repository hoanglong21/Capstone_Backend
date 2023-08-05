package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.model.Progress;
import com.capstone.project.model.User;

import java.util.List;

public interface ProgressService {

    List<Progress> getAllProgresses();

    Progress createProgress(Progress progress);

    Progress getProgressById(int id) throws ResourceNotFroundException;

    Progress updateProgress(int id, Progress progressDetails) throws ResourceNotFroundException;

    Boolean deleteProgress(int id) throws ResourceNotFroundException;

    Progress updateScore(int userId, int cardId, int score);

    Progress customUpdateProgress(User user, Card card, boolean isStar, String picture, String audio, String note, String status);

    Boolean resetProgress(int userId, int studySetId);

    Progress getProgressByUserIdAndCardId(int userId, int cardId) throws ResourceNotFroundException;
}
