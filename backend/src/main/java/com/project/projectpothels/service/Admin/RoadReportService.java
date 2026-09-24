package com.project.projectpothels.service.Admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.projectpothels.Repository.Admin.RoadReportRepository;
import com.project.projectpothels.model.Admin.RoadReport;

@Service
public class RoadReportService {
    
    @Autowired
    private RoadReportRepository repository;
    
   public List<RoadReport> getAllReports() {

    return repository.findAll().stream().sorted((a, b) -> {
                int statusCompare =
                    Integer.compare(
                        getReportOrder(a.getStatus()),
                        getReportOrder(b.getStatus())
                    );

                if (statusCompare != 0) {
                    return statusCompare;
                }

                if (a.getCreatedAt() == null || b.getCreatedAt() == null) {
                    return 0;
                }

                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }).toList();
}

    private int getReportOrder(String status){
        if (status == null) {
            return 99;
        }
        switch (status.toUpperCase()) {
            case "PENDING":
                return 1;
            case "VERIFIED":
                return 2;
             case "REJECTED":
                return 3;
            case "ASSIGNED":
                return 4; 
            case "INSPECTION_COMPLETED":
                return 5;
            case "IN-PROGRESS":
                return 6;
            case "COMPLETED":
                return 7;
            default:
                return 99;
        }
    }

    public RoadReport getReport(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Report Not Found"));
    }

    public RoadReport saveReport(RoadReport report){
        if (report.getReportCode() == null || report.getReportCode().isBlank()) {
            report.setReportCode("RP-" + (1000 + (int)(Math.random()*9000)));
        }
        if  (report.getStatus() == null){
            report.setStatus("PENDING");
        }

        report.calculatePriority();

        return repository.save(report);
    }

    public void verifyReport(Long id){
        RoadReport report = getReport(id);
        report.setVerified(true);
        report.setStatus("VERIFIED");
        if (report.getVerifiedAt() == null) {
            report.setVerifiedAt(LocalDateTime.now());
        }
        repository.save(report);
    }

    public void rejectReport(Long id,String reason){
        RoadReport report = getReport(id);
        if ("COMPLETED".equalsIgnoreCase(report.getStatus())) {
            throw new RuntimeException("Completed Report Cannot Be Rejected");
        }
        report.setVerified(false);
        report.setStatus("REJECTED");
        report.setRejectionReason(reason);
        report.setRejectedAt(LocalDateTime.now());
        
        repository.save(report);
    }

    public List<RoadReport> getPriorityReports(){
        List<RoadReport> reports = repository.findByVerifiedTrueOrderByPriorityScoreDesc();

        return reports.stream().filter(report -> !"COMPLETED".equalsIgnoreCase(report.getStatus()) && !"ASSIGNED".equalsIgnoreCase(report.getStatus()) && !"IN-PROGRESS".equalsIgnoreCase(report.getStatus()))
    .peek(this::calculateRealPriority).sorted((a,b) -> Integer.compare(b.getPriorityScore(), a.getPriorityScore())).toList();  
        
    }

    public long totalReports(){
        return repository.count();
    }

    public long pendingReports(){
        return repository.countByStatus("PENDING");
    }

    public long criticalReports(){
        return repository.countBySeverity("CRITICAL");
    }   
    
    public long completedReports(){
        return repository.countByStatus("COMPLETED");
    }

    public long verifiedReports(){
        return repository.countByVerifiedTrue();
    }

    public long inProgressReports(){
        return repository.countByStatus("IN-PROGRESS");
    }

    public long rejectedReports(){
        return repository.countByStatus("REJECTED");
    }

    public long countSeverity(String severity){
        return repository.countBySeverity(severity);
    }

    // public List<RoadReport> searchReports(String search){
    //     return repository.findByAreaContainingIgnoreCaseOrRoadNameContainingIgnoreCase(search, search);
    // }

    public List<RoadReport> searchReports(String search) {

    return repository.findByAreaContainingIgnoreCaseOrRoadNameContainingIgnoreCase( search,search).stream().sorted((a, b) -> {

            int statusCompare =
                Integer.compare(
                    getReportOrder(a.getStatus()),
                    getReportOrder(b.getStatus())
                );

            if (statusCompare != 0) {

                return statusCompare;
            }

            if (a.getCreatedAt() == null || b.getCreatedAt() == null) {
                return 0;
            }

            return b.getCreatedAt().compareTo(a.getCreatedAt());
        }).toList();
}
    public List<RoadReport> searchPriorityReports(String search){
        return repository.findByAreaContainingIgnoreCaseOrRoadNameContainingIgnoreCase(search, search)
                .stream()
                .sorted((a, b) -> Integer.compare(b.getPriorityScore(), a.getPriorityScore()))
                .toList();
    }

    public long criticalPriority(){
        return getPriorityReports().stream().filter(r -> r.getPriorityScore() >= 80).count();
    }
    public long highPriority(){
        return getPriorityReports().stream().filter(r -> r.getPriorityScore() >= 60 && r.getPriorityScore() < 80).count();
    }
    public long mediumPriority(){
        return getPriorityReports().stream().filter(r -> r.getPriorityScore() >= 40 && r.getPriorityScore() < 60).count();
    }
    public long lowPriority(){
        return getPriorityReports().stream().filter(r -> r.getPriorityScore() < 40).count();
    }

    private void calculateRealPriority(RoadReport report){
        int severityScore = 0;
        if (report.getSeverity() != null) {
            switch (report.getSeverity().toUpperCase()) {

                case "CRITICAL":
                    severityScore = 30;
                    break;
            
                case "HIGH":
                    severityScore = 24;
                    break;

                case "MEDIUM":
                    severityScore = 16;
                    break;
                
                case "LOW":
                    severityScore = 8;
                    break;

                
                default:
                    break;
            }
        }

        int trafficScore = 10;

        if (report.getProblemType() != null) {
            String problem = report.getProblemType().toUpperCase();

            if (problem.contains("COLLAPSE") || problem.contains("WATER")) {
                trafficScore = 30;
            }else if (problem.contains("POTHOLE") || problem.contains("DAMAGED")) {
                trafficScore = 22;
            }
            else if (problem.contains(("CRACK"))) {
                trafficScore = 15;
            }
        }

        long sameRoadReports=repository.countByAreaIgnoreCaseAndRoadNameIgnoreCase(report.getArea(), report.getRoadName());

        int reportScore;

        if (sameRoadReports >= 4) {
            reportScore = 20;
        }
        else if (sameRoadReports == 3) {
            reportScore = 15;
        }
        else if (sameRoadReports == 2) {
            reportScore = 10;
        }
        else{
            reportScore = 5;
        }

        int waitingScore = 5;

        if (report.getCreatedAt() != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(report.getCreatedAt().toLocalDate(), java.time.LocalDate.now());

            if (days >= 7) {
                waitingScore = 20;
            }
            else if (days >= 4) {
                waitingScore = 15;
            }
            else if (days >= 2) {
                waitingScore = 10;
            }
        }

        int total = severityScore + trafficScore + reportScore + waitingScore;

        report.setSeverityScore(severityScore);

        report.setTrafficScore(trafficScore);

        report.setWaitingScore(waitingScore);

        report.setPriorityScore(total);
    }
}
