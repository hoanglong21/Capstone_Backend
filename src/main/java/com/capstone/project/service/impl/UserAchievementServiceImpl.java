package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Feedback;
import com.capstone.project.model.History;
import com.capstone.project.model.User;
import com.capstone.project.model.UserAchievement;
import com.capstone.project.repository.UserAchievementRepository;
import com.capstone.project.repository.UserRepository;
import com.capstone.project.service.UserAchievementService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserAchievementServiceImpl implements UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;

    private final EntityManager entityManager;

    public UserAchievementServiceImpl(UserAchievementRepository userAchievementRepository, EntityManager entityManager) {
        this.userAchievementRepository = userAchievementRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<UserAchievement> getAll() {
        return userAchievementRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public UserAchievement createUserAchievement(UserAchievement userAchievement) {
        try {
            userAchievement.setCreated_date(new Date());
            return userAchievementRepository.save(userAchievement);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate entry, make sure id of user and achievement are not duplicated");
        }
    }

    @Override
    public UserAchievement getUserAchievementById(int id) throws ResourceNotFroundException {
        UserAchievement userAchievement = userAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Achievement not exist with id: " + id));
        return userAchievement;
    }

    @Override
    public List<UserAchievement> getUserAchievementByUserId(int id) {
        return userAchievementRepository.getUserAchievementByUserId(id);
    }

    @Override
    public Map<String, Object> filterUserAchievement(int userId, int achievementId, String fromCreated, String toCreated, String sortBy, String direction, int page, int size) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String jpql = "FROM UserAchievement f LEFT JOIN FETCH f.achievement WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();

        if (achievementId != 0) {
            jpql += " AND f.achievement.id = :achievementId ";
            params.put("achievementId", achievementId);
        }

        if (userId != 0) {
            jpql += " AND f.user.id = :userId ";
            params.put("userId", userId);
        }

        if (fromCreated != null && !fromCreated.equals("")) {
            jpql += " AND DATE(f.created_date) >= :fromCreated ";
            params.put("fromCreated", formatter.parse(fromCreated));
        }
        if (toCreated != null && !toCreated.equals("")) {
            jpql += " AND DATE(f.created_date) <= :toCreated ";
            params.put("toCreated", formatter.parse(toCreated));
        }
        
        if(sortBy != null && !sortBy.equals("") && direction != null && !direction.equals("")) {
            sortBy = "f." + sortBy;
            jpql += " ORDER BY " + sortBy + " " + direction;
        }

        TypedQuery<UserAchievement> query = entityManager.createQuery(jpql, UserAchievement.class);

        // Set parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination
        int offset = (page - 1) * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);

        List<UserAchievement> userAchievements = query.getResultList();

        int totalItems = getTotalCount(jpql, params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", userAchievements);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

    private int getTotalCount(String jpql, Map<String, Object> params) {
        // Remove the ORDER BY clause from the original JPQL query
        int orderByIndex = jpql.toUpperCase().lastIndexOf("ORDER BY");
        if (orderByIndex != -1) {
            jpql = jpql.substring(0, orderByIndex);
        }

        // Construct the count query by adding the COUNT function and removing the FETCH JOIN
        String countJpql = "SELECT COUNT(f) " + jpql.replace("LEFT JOIN FETCH f.achievement", "");

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);

        // Set parameters for the count query
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        Long countResult = countQuery.getSingleResult();
        return countResult != null ? countResult.intValue() : 0;
    }
}
