package com.project.projectpothels.Repository.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.projectpothels.model.Admin.Assignment;

import jakarta.persistence.Entity;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>{
    
    List<Assignment> findByEngineerId(Long engineerId);
    
    List<Assignment> findByEngineer_Email(String email);
    
    List<Assignment> findByStatus(String status);

    long countByStatus(String status);

    Optional<Assignment> findByReportId(Long reportId);
    
}
