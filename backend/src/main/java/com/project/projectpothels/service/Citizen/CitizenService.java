package com.project.projectpothels.service.Citizen;

import com.project.projectpothels.Repository.Admin.AssignmentRepository;
import com.project.projectpothels.Repository.Admin.RepairReviewRepository;
import com.project.projectpothels.Repository.Admin.ReportPhotoRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.projectpothels.Repository.RegisRepository;
import com.project.projectpothels.Repository.Admin.RoadReportRepository;
import com.project.projectpothels.model.RegisModel;
import com.project.projectpothels.model.Admin.Assignment;
import com.project.projectpothels.model.Admin.RepairReview;
import com.project.projectpothels.model.Admin.ReportPhotos;
import com.project.projectpothels.model.Admin.RoadReport;

@Service
public class CitizenService {

    @Autowired
    private AssignmentRepository assignrepo;

    private final ReportPhotoRepository reportPhotoRepository;

    @Autowired
    private RoadReportRepository roadrepo;

    @Autowired
    private RegisRepository regisrepo;

    @Autowired
    private RepairReviewRepository reviewRepository;
    

    @Autowired
    public CitizenService(ReportPhotoRepository reportPhotoRepository) {
        this.reportPhotoRepository = reportPhotoRepository;
    }
    public RegisModel getCitizen(String email) {
        return regisrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Citizen Not Found"));
    }

    public RoadReport createReport(RoadReport report) {
        report.setStatus("PENDING");
        report.calculatePriority();
        return roadrepo.save(report);
    }

    public List<RoadReport> getMyReports(String email) {
        return roadrepo.findByCitizenEmailOrderByCreatedAtDesc(email);
    }

    public long totalReport(String email) {
        return roadrepo.countByCitizenEmail(email);
    }

    public long pendingReport(String email) {
        return roadrepo.countByCitizenEmailAndStatus(email, "PENDING");
    }

    public long inProgressReport(String email) {
        return roadrepo.countByCitizenEmailAndStatus(email, "IN-PROGRESS");
    }

    public long completedReport(String email) {
        return roadrepo.countByCitizenEmailAndStatus(email, "COMPLETED");
    }

    public RoadReport getMyReport(Long id, String email) {
        return roadrepo.findById(id).filter(report -> email.equals(report.getCitizenEmail()))
                .orElseThrow(() -> new RuntimeException("Report Not Found"));
    }

    private String savePhoto(MultipartFile photo, String folderName) {

        if (photo == null || photo.isEmpty()) {
            return null;
        }
        try {
            String uploadDir = "uploads/" + folderName + "/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + folderName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException(
                 "Image upload failed", e);
        }
    }

    public RoadReport createReport(RoadReport report, MultipartFile photo) {
        report.setStatus("PENDING");
        report.calculatePriority();
        String photoPath = savePhoto(photo, "citizen");
        report.setCitizenPhoto(photoPath);
        return roadrepo.save(report);
    }
public void saveReportPhotos(
        RoadReport report,
        List<MultipartFile> photos) {

    if (photos == null || photos.isEmpty()) {
        return;
    }

    if (photos.size() > 3) {
        throw new RuntimeException(
                "Maximum 3 photos are allowed"
        );
    }

    for (MultipartFile photo : photos) {

        if (photo == null || photo.isEmpty()) {
            continue;
        }

        String photoPath = savePhoto(photo,"citizen");

        ReportPhotos reportPhoto =
                new ReportPhotos();

        reportPhoto.setReport(report);
        reportPhoto.setPhotoPath(photoPath);

        ReportPhotos savePhoto = reportPhotoRepository.save(reportPhoto);
        report.getPhotos().add(savePhoto);
    }
}
public Map<String, Object> getTrackingDetails(Long reportId, String email){
    RoadReport report = getMyReport(reportId, email);
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("reportId", report.getId());
    data.put("reportCode", report.getReportCode());
    data.put("status", report.getStatus());
    data.put("submittedAt", report.getCreatedAt());
    data.put("verifiedAt", report.getVerifiedAt());
    data.put("rejectedAt", report.getRejectedAt());
    data.put("rejectionReason", report.getRejectionReason());

    Assignment assign = assignrepo.findByReportId(reportId).orElse(null);

    if (assign != null) {
        data.put("assignmentId", assign.getId());
        data.put("engineerName",assign.getEngineer().getName());

        data.put("assignedAt",assign.getAssignedAt());

        data.put( "inspectionCompletedAt",assign.getInspectionCompletedAt());

        data.put("inProgressAt",assign.getInProgressAt());

        data.put("completedAt",assign.getCompletedAt());
        } 
        else {

        data.put("assignmentId", null);
        data.put("engineerName", null);
        data.put("assignedAt", null);

        data.put("inspectionCompletedAt",null);

        data.put("inProgressAt", null);
        data.put("completedAt", null);
    }
    return data;
}

public RepairReview submitReview(Long assignmentId, String citizenEmail, Integer rating, String reviewText){
    Assignment assignment = assignrepo.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment Not Found"));
    if (!assignment.getReport().getCitizenEmail().equalsIgnoreCase(citizenEmail)) {
        throw new RuntimeException("You Cannot Review This Repair");
    }

    if (!"COMPLETED".equalsIgnoreCase(assignment.getStatus())) {
        throw new RuntimeException("Review Allowed Only After Repair Completion");
    }

    if (rating == null || rating < 1 || rating > 5) {
        throw new RuntimeException("Rating Must Be Between 1 and 5");
    }

    if (reviewRepository.findByAssignmentId(assignmentId).isPresent()) {
        throw new RuntimeException("Review Already Submitted");
    }

    RepairReview repairReview = new RepairReview();

    repairReview.setAssignment(assignment);
    repairReview.setRating(rating);
    repairReview.setReview(reviewText);

    return reviewRepository.save(repairReview);
}

}