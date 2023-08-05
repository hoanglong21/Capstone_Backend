package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Class;
import com.capstone.project.model.ClassLearner;
import com.capstone.project.model.UserAchievement;
import com.capstone.project.model.UserSetting;
import com.capstone.project.repository.ClassLearnerRepository;
import com.capstone.project.service.ClassLearnerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ClassLeanerServiceImpl implements ClassLearnerService {
    @PersistenceContext
    private EntityManager em;

    private final ClassLearnerRepository classLearnerRepository;

    public ClassLeanerServiceImpl(ClassLearnerRepository classLearnerRepository) {
        this.classLearnerRepository = classLearnerRepository;
    }

    @Override
    public List<ClassLearner> getAll() {
        return classLearnerRepository.findAll();
    }

    @Override
    public ClassLearner createClassLearner(ClassLearner classLearner) {
        try {
            classLearner.setCreated_date(new Date());
            return classLearnerRepository.save(classLearner);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate entry, make sure id of user and class are not duplicated");
        }
    }

    @Override
    public ClassLearner updateClassLearner(ClassLearner classroomLearner, int id) throws ResourceNotFroundException {
        ClassLearner classLearner = classLearnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("ClassLearner not exist with id:" + id));
        classLearner.set_accepted(classroomLearner.is_accepted());
        return classLearnerRepository.save(classLearner);
    }

    @Override
    public Boolean deleteClassLearner(int userid, int classid) throws ResourceNotFroundException {
        ClassLearner classLearner = classLearnerRepository.findByUserIdAndClassroomId(userid,classid);
        classLearnerRepository.delete(classLearner);
        return true;
    }

    @Override
    public Boolean deleteClassLearnerById(int id) throws ResourceNotFroundException {
        ClassLearner classLearner = classLearnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Not exist with id:" + id));
        classLearnerRepository.delete(classLearner);
        return true;
    }


    @Override
    public ClassLearner getClassLeanerById(int id) throws ResourceNotFroundException {
        ClassLearner classLearner = classLearnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("Not exist with id: " + id));
        return classLearner;
    }

    @Override
    public ClassLearner getClassLeanerByUserId(int id) {
        return classLearnerRepository.getClassLeanerByUserId(id);
    }

    @Autowired
    private EntityManager entityManager;
    @Override
    public Map<String, Object> filterClassLearner(int userId, int classId, String fromCreated, String toCreated,Boolean isAccepted, String sortBy, String direction, int page, int size) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String jpql = "FROM ClassLearner f LEFT JOIN FETCH f.classroom WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();

        if (classId != 0) {
            jpql += " AND f.classroom.id = :classId ";
            params.put("classId", classId);
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

        if (isAccepted != null) {
            jpql += " AND is_accepted = :isAccepted";
            params.put("isAccepted", isAccepted);

        }

        sortBy = "f." + sortBy;


        jpql += " ORDER BY " + sortBy + " " + direction;

        TypedQuery<ClassLearner> query = entityManager.createQuery(jpql, ClassLearner.class);

        // Set parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination
        int offset = (page - 1) * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);

        List<ClassLearner> classLearners = query.getResultList();

        int totalItems = getTotalCount(jpql, params);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", classLearners);
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
        String countJpql = "SELECT COUNT(f) " + jpql.replace("LEFT JOIN FETCH f.classroom", "");

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);

        // Set parameters for the count query
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        Long countResult = countQuery.getSingleResult();
        return countResult != null ? countResult.intValue() : 0;
    }



    @Override
    public Map<String, Object> filterGetLearner(int userId, int classId, Boolean isAccepted, String sortBy, String direction, int page, int size) {

        int offset = (page - 1) * size;

        String query = "SELECT cl.id, u.username as username,u.avatar as avatar, cl.is_accepted as accepted, cl.created_date as created_date\n" +
                "FROM class_learner cl \n" +
                "INNER JOIN user u  ON u.id = cl.user_id\n" +
                "INNER JOIN class c on c.id = cl.class_id\n" +
                "WHERE 1=1 ";

        Map<String, Object> parameters = new HashMap<>();

        if (classId != 0) {
            query += " AND class_id = :classId";
            parameters.put("classId", classId);
        }

        if (userId != 0) {
            query += " AND user_id = :userId";
            parameters.put("userId", userId);
        }

        if (isAccepted != null) {
            query += " AND is_accepted = :isAccepted";
            parameters.put("isAccepted", isAccepted);

        }

        query += " ORDER BY cl." + sortBy + " " + direction;

        Query q = em.createNativeQuery(query, "ClassLearnerListMapping");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        int totalItems = q.getResultList().size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        q.setFirstResult(offset);
        q.setMaxResults(size);

        List<ClassLearner> resultList = q.getResultList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", resultList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", totalItems);

        return response;
    }

}