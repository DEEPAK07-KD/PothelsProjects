package com.project.projectpothels.Repository.Admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.projectpothels.model.Admin.ReportPhotos;

public interface ReportPhotoRepository extends JpaRepository<ReportPhotos, Long> {
    List<ReportPhotos> findByReportId(Long reportId);
}
