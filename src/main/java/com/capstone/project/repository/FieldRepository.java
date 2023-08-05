package com.capstone.project.repository;

import com.capstone.project.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    @Query(value = "SELECT * FROM field f WHERE f.type_id = :id", nativeQuery = true)
    List<Field> findFieldsByType_Id(int id);
}
