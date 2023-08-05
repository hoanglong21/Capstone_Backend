package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Feedback;
import com.capstone.project.model.History;
import com.capstone.project.model.User;
import com.capstone.project.repository.HistoryRepository;
import com.capstone.project.service.HistoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, JdbcTemplate jdbcTemplate) {
        this.historyRepository = historyRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<History> getAll() {
        return historyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public History createHistory(History history) {
        history.setDatetime(new Date());
        return historyRepository.save(history);
    }

    @Override
    public History getHistoryById(int id) throws ResourceNotFroundException {
        History history = historyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFroundException("History not exist with id: " + id));
        return history;
    }

    @Override
    public Map<String, Object> filterHistory(int userId, int destinationId, int typeId, int categoryId, String fromDatetime, String toDatetime,
                                       String sortBy, String direction, int page, int size) {
        String sql = "SELECT h.* FROM history h";

//        if(userId!=0) {
//            sql += " LEFT JOIN user u ON h.user_id = u.id";
//        }
        if (typeId==2) {
            sql += " LEFT JOIN studyset s ON h.studyset_id = s.id";
        }
//        if (typeId==3) {
//            sql += " LEFT JOIN class c ON h.class_id = c.id";
//        }

        sql += " WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if(userId!=0) {
            sql += " AND h.user_id = ? ";
            params.add(userId);
        } if(typeId==2 && destinationId!=0) {
            sql += " AND h.studyset_id = ? ";
            params.add(destinationId);
        } if (typeId==3 && destinationId!=0) {
            sql += " AND h.class_id = ? ";
            params.add(destinationId);
        }

        if(typeId==2 && categoryId!=0) {
            sql += " AND s.type_id = ? ";
            params.add(categoryId);
        }

        sql += " AND h.type_id = ?";
        params.add(typeId);


        if (fromDatetime != null && !fromDatetime.isEmpty()) {
            sql += " AND DATE(h.datetime) >= ? ";
            params.add(fromDatetime);
        }

        if (toDatetime != null && !toDatetime.isEmpty()) {
            sql += " AND DATE(h.datetime) <= ? ";
            params.add(toDatetime);
        }

        sortBy = "h." + sortBy;

        sql += " ORDER BY " + sortBy + " " + direction;

        // Count total items
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS countQuery";
        long totalItems = jdbcTemplate.queryForObject(countSql, Long.class, params.toArray());

        // Apply pagination
        int offset = (page - 1) * size;
        sql += " LIMIT ? OFFSET ?";
        params.add(size);
        params.add(offset);

        List<History> historyList = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<>(History.class));
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", historyList);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }
}
