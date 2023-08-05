package com.capstone.project.repository;

import com.capstone.project.model.AttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentTypeRepository extends JpaRepository<AttachmentType,Integer> {
}
