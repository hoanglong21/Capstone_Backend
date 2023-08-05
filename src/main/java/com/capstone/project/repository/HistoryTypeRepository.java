package com.capstone.project.repository;

import com.capstone.project.model.HistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryTypeRepository extends JpaRepository<HistoryType, Integer> {
}
