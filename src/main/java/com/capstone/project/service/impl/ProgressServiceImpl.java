package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Card;
import com.capstone.project.model.Progress;
import com.capstone.project.model.User;
import com.capstone.project.repository.ProgressRepository;
import com.capstone.project.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public List<Progress> getAllProgresses() {
        return progressRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Progress createProgress(Progress progress) {
        try {
            return progressRepository.save(progress);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate entry, make sure id of user and card are not duplicated");
        }
    }

    @Override
    public Progress getProgressById(int id) throws ResourceNotFroundException {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Progress not exist with id: " + id));
        return progress;
    }

    @Override
    public Progress updateProgress(int id, Progress progressDetails) throws ResourceNotFroundException {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Progress not exist with id: " + id));
        progress.setStatus(progressDetails.getStatus());
        progress.setRight(progressDetails.getRight());
        progress.setWrong(progressDetails.getWrong());
        progress.setTotal_wrong(progressDetails.getTotal_wrong());
        progress.set_star(progressDetails.is_star());
        progress.setNote(progressDetails.getNote());
        progress.setAudio(progressDetails.getAudio());
        progress.setPicture(progressDetails.getPicture());

        Progress updateProgress = progressRepository.save(progress);
        return updateProgress;
    }

    @Override
    public Boolean deleteProgress(int id) throws ResourceNotFroundException {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Progress not exist with id: " + id));
        progressRepository.delete(progress);
        return true;
    }

    @Override
    public Progress updateScore(int userId, int cardId, int score) {
        Progress progress = progressRepository.findByCardIdAndUserId(userId, cardId);
        if(progress == null) {
            progress = Progress.builder()
                    .user(User.builder().id(userId).build())
                    .card(Card.builder().id(cardId).build())
                    .build();
        }
        if(score>0) {
            progress.setRight(progress.getRight()+1);
        } else if(score<0) {
            progress.setWrong(progress.getWrong()+1);
            progress.setTotal_wrong(progress.getTotal_wrong()+1);
        }

        if (progress.getRight()==0 && progress.getWrong()==0) {
            progress.setStatus("not studied");
        } else if(progress.getRight()>0 || progress.getWrong()>0) {
            progress.setStatus("still learning");
        } else if(progress.getRight()>=2) {
            progress.setStatus("mastered");
        }
        return progressRepository.save(progress);
    }

    @Override
    public Progress customUpdateProgress(User user, Card card, boolean isStar, String picture, String audio, String note, String status) {
        Progress progress = progressRepository.findByCardIdAndUserId(user.getId(), card.getId());
        if(progress == null) { 
            progress = Progress.builder()
                    .user(user)
                    .card(card)
                    .status("not studied")
                    .build();
        }
        progress.set_star(isStar);
        progress.setNote(note);
        progress.setAudio(audio);
        progress.setPicture(picture);
        if(status!=null && !progress.getStatus().equals("master")) {
            progress.setStatus(status);
        }
        return progressRepository.save(progress);
    }

    @Override
    public Boolean resetProgress(int userId, int studySetId) {
        List<Progress> progressList = progressRepository.findByStudySetIdAndUserId(userId, studySetId);
        for(Progress item: progressList) {
            item.setStatus("not studied");
            item.setRight(0);
            item.setWrong(0);
        }
        return true;
    }

    @Override
    public Progress getProgressByUserIdAndCardId(int userId, int cardId) throws ResourceNotFroundException {
        Progress progress = progressRepository.findByCardIdAndUserId(userId, cardId);
        if(progress == null) {
            throw new ResourceNotFroundException("Progress not exist with userId: " + userId + " and cardId: " + cardId);
        }
        return progress;
    }
}
