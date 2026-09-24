package com.project.projectpothels.model.Admin;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "road_report")
public class RoadReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reportCode;

    private String citizenName;
    private String citizenEmail;

    private String area;
    private String roadName;
    private String landmark;
    private String problemType;
    private String severity;
    private String status;
    private String citizenPhoto;
    private String citizenPhone;
    private Double latitude;
    private Double longitude;
    private LocalDateTime rejectedAt;
    private LocalDateTime verifiedAt;

    @Column(length = 1000)
    private String rejectionReason;

    @Column(length = 2000)
    private String description;

    private Integer priorityScore;

    private LocalDateTime createdAt;

    private boolean verified = false;

    @Transient
    private int severityScore,trafficScore,reportCountScore,waitingScore;

    @OneToMany(mappedBy = "report",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReportPhotos> photos = new ArrayList<>();
    public RoadReport() {
    }

    public RoadReport(Long id, String reportCode, String citizenName, String citizenEmail, String area, String roadName,
            String landmark, String problemType, String severity, String status, Double latitude, Double longitude,
            String description, Integer priorityScore, LocalDateTime createdAt,String citizenPhoto,String citizenPhone,
            String rejectionReason, LocalDateTime  rejectedAt) {
        this.id = id;
        this.reportCode = reportCode;
        this.citizenName = citizenName;
        this.citizenEmail = citizenEmail;
        this.area = area;
        this.roadName = roadName;
        this.landmark = landmark;
        this.problemType = problemType;
        this.severity = severity;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.priorityScore = priorityScore;
        this.createdAt = createdAt;
        this.citizenPhoto = citizenPhoto;
        this.citizenPhone = citizenPhone;
        this.rejectionReason = rejectionReason;
        this.rejectedAt = rejectedAt;
    }

    @PrePersist
    public void beforeSave() {

        if (reportCode == null || reportCode.isBlank()) {
            reportCode = "RP-" + System.currentTimeMillis();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = "PENDING";
        }

        calculatePriority();
    }

    @PreUpdate
    public void beforeUpdate() {
        calculatePriority();
    }

    public void calculatePriority() {
        if (severity == null) {
            priorityScore = 0;
            return;
        }
        switch (severity.toUpperCase()) {
            case "CRITICAL":
                priorityScore = 90;
                break;

            case "HIGH":
                priorityScore = 70;
                break;

            case "MEDIUM":
                priorityScore = 50;
                break;

            case "LOW":
                priorityScore = 30;
                break;

            default:
                priorityScore = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(Integer priorityScore) {
        this.priorityScore = priorityScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCitizenEmail() {
        return citizenEmail;
    }

    public void setCitizenEmail(String citizenEmail) {
        this.citizenEmail = citizenEmail;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCitizenPhoto() {
        return citizenPhoto;
    }

    public void setCitizenPhoto(String citizenPhoto) {
        this.citizenPhoto = citizenPhoto;
    }

    public boolean isVerified(){
        return verified;
    }

    public void setVerified(boolean verified){
        this.verified = verified;
    }

    public int getSeverityScore() {
        return severityScore;
    }

    public void setSeverityScore(int severityScore) {
        this.severityScore = severityScore;
    }

    public int getTrafficScore() {
        return trafficScore;
    }

    public void setTrafficScore(int trafficScore) {
        this.trafficScore = trafficScore;
    }

    public int getReportCountScore() {
        return reportCountScore;
    }

    public void setReportCountScore(int reportCountScore) {
        this.reportCountScore = reportCountScore;
    }

    public int getWaitingScore() {
        return waitingScore;
    }

    public void setWaitingScore(int waitingScore) {
        this.waitingScore = waitingScore;
    }

    public String getCitizenPhone() {
        return citizenPhone;
    }

    public void setCitizenPhone(String citizenPhone) {
        this.citizenPhone = citizenPhone;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public List<ReportPhotos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ReportPhotos> photos) {
        this.photos = photos;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    

}
