package com.project.projectpothels.Repository.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.projectpothels.model.Admin.RoadReport;
import java.util.List;


@Repository
public interface RoadReportRepository extends JpaRepository<RoadReport, Long>{
    long countByStatus(String status);

    long countBySeverity(String severity);

    List<RoadReport> findByStatus(String status);

    List<RoadReport> findAllByOrderByPriorityScoreDesc();

    List<RoadReport> findTop5ByOrderByPriorityScoreDesc();

    List<RoadReport> findByCitizenEmailOrderByCreatedAtDesc(
            String citizenEmail
    );
    List<RoadReport> findByAreaContainingIgnoreCaseOrRoadNameContainingIgnoreCase(String area,String roadName);

    long countByCitizenEmail(String citizenEmail);

    long countByCitizenEmailAndStatus(String citizenEmail,String status);

    long countByVerifiedTrue();

    List<RoadReport> findByVerifiedTrueOrderByPriorityScoreDesc();

    long countByAreaIgnoreCaseAndRoadNameIgnoreCase(String area, String roadName);
    
}
