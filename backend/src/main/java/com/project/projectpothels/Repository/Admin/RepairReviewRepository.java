package com.project.projectpothels.Repository.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.projectpothels.model.Admin.RepairReview;

@Repository
public interface RepairReviewRepository extends JpaRepository<RepairReview, Long>{
    Optional<RepairReview> findByAssignmentId(Long assignmentId);

    List<RepairReview> findByAssignment_Engineer_Id(Long engineerId);
    
}
