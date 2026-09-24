package com.project.projectpothels.model.Admin;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "report_Photo")
public class ReportPhotos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id",nullable = false)
    @JsonBackReference
    private RoadReport report;

    private String photoPath;

    public ReportPhotos() {
    }

    public ReportPhotos(Long id, RoadReport report, String photoPath) {
        this.id = id;
        this.report = report;
        this.photoPath = photoPath;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    
}
