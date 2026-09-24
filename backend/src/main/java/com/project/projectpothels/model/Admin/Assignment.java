package com.project.projectpothels.model.Admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "repair_assignment")
public class Assignment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private RoadReport report;

    @ManyToOne
    @JoinColumn(name = "engineer_id", nullable = false)
    private EngineerModel engineer;
    
    private LocalDate assignedDate;

    private LocalDateTime assignedAt;

    private LocalDateTime inspectionCompletedAt;

    private LocalDateTime inProgressAt;

    private LocalDateTime completedAt;

    private String status;

    private String inspectionNotes;

    private String beforePhoto;

    private String afterPhoto;

    public Assignment() {
    }

    public Assignment(Long id, RoadReport report, EngineerModel engineer, LocalDate assignedDate, String status, String inspectionNotes, String beforePhoto, String afterPhoto) {
        this.id = id;
        this.report = report;
        this.engineer = engineer;
        this.assignedDate = assignedDate;
        this.status = status;
        this.inspectionNotes = inspectionNotes;
        this.beforePhoto = beforePhoto;
        this.afterPhoto = afterPhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoadReport getReport() {
        return report;
    }

    public void setReport(RoadReport report) {
        this.report = report;
    }

    public EngineerModel getEngineer() {
        return engineer;
    }

    public void setEngineer(EngineerModel engineer) {
        this.engineer = engineer;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInspectionNotes() {
        return inspectionNotes;
    }

    public void setInspectionNotes(String inspectionNotes) {
        this.inspectionNotes = inspectionNotes;
    }

    public String getBeforePhoto() {
        return beforePhoto;
    }

    public void setBeforePhoto(String beforePhoto) {
        this.beforePhoto = beforePhoto;
    }

    public String getAfterPhoto() {
        return afterPhoto;
    }

    public void setAfterPhoto(String afterPhoto) {
        this.afterPhoto = afterPhoto;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getInspectionCompletedAt() {
        return inspectionCompletedAt;
    }

    public void setInspectionCompletedAt(LocalDateTime inspectionCompletedAt) {
        this.inspectionCompletedAt = inspectionCompletedAt;
    }

    public LocalDateTime getInProgressAt() {
        return inProgressAt;
    }

    public void setInProgressAt(LocalDateTime inProgressAt) {
        this.inProgressAt = inProgressAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    

}
