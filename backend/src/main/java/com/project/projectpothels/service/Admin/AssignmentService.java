package com.project.projectpothels.service.Admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.projectpothels.Repository.Admin.AssignmentRepository;
import com.project.projectpothels.Repository.Admin.EngineerRepository;
import com.project.projectpothels.Repository.Admin.RoadReportRepository;
import com.project.projectpothels.model.Admin.Assignment;
import com.project.projectpothels.model.Admin.EngineerModel;
import com.project.projectpothels.model.Admin.RoadReport;

import ch.qos.logback.core.joran.conditional.IfAction;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignRepo;

    @Autowired
    private EngineerRepository engineerRepo;

    @Autowired
    private RoadReportRepository reportRepo;

    public Assignment assignReport(Long reportId,Long engineerId,LocalDate date){
        RoadReport report = reportRepo.findById(reportId).orElseThrow(() -> new RuntimeException("Report Not Found"));

        EngineerModel engineer = engineerRepo.findById(engineerId).orElseThrow(() -> new RuntimeException("Engineer Not Found"));

        Assignment assignment = new Assignment();

        assignment.setReport(report);
        assignment.setEngineer(engineer);
        assignment.setAssignedDate(date);
        assignment.setStatus("ASSIGNED");
        assignment.setAssignedAt(LocalDateTime.now());

        report.setStatus("ASSIGNED");
        engineer.setStatus("ASSIGNED");

        reportRepo.save(report);
        engineerRepo.save(engineer);

        return assignRepo.save(assignment);
    }

    public List<Assignment> getAllAssignments(){
        return assignRepo.findAll();
    }

    public List<Assignment> getAssignmentsByEngineer(String email) {
        return assignRepo.findByEngineer_Email(email);
    }

    public Assignment getAssignment(Long assignmentId, String engineerEmail){
        Assignment assignment = assignRepo.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment Not Found"));

        if (!assignment.getEngineer().getEmail().equalsIgnoreCase(engineerEmail)) {
            throw new RuntimeException("This Task Is Not Assigned To This Engineer");
        }
        return assignment;
    }

    private void updateEngineerStatus(EngineerModel engineer){
        List<Assignment> assignments = assignRepo.findByEngineerId(engineer.getId());

        boolean hasActiveTask = assignments.stream().anyMatch(a -> {
            String status = a.getStatus();
            return status != null && !status.equalsIgnoreCase("COMPLETED");
        });

        if(hasActiveTask){
            engineer.setStatus("ASSIGNED");
        }
        else{
            engineer.setStatus("AVAILABLE");
        }
        engineerRepo.save(engineer);
    }

    public Assignment updateTask(
        Long assignmentId,
        String email,
        String severity,
        String status,
        String notes,
        MultipartFile beforePhoto,
        MultipartFile afterPhoto) {

    Assignment assignment = assignRepo
            .findById(assignmentId)
            .orElseThrow(() ->
                    new RuntimeException("Assignment Not Found"));

    if (!assignment.getEngineer()
            .getEmail()
            .equalsIgnoreCase(email)) {

        throw new RuntimeException(
                "You are not allowed to update this task"
        );
    }

    if ("COMPLETED".equalsIgnoreCase(
            assignment.getStatus())) {

        throw new RuntimeException(
                "This Repair Is Already Completed And Cannot Be Updated Again"
        );
    }

    RoadReport report =
            assignment.getReport();

    if (severity != null &&
            !severity.isBlank()) {

        report.setSeverity(severity);
    }

    if (notes != null) {

        assignment.setInspectionNotes(notes);
    }

    if (beforePhoto != null &&
            !beforePhoto.isEmpty()) {

        String beforePhotoPath =
                savePhoto(
                        beforePhoto,
                        "engineer/before"
                );

        assignment.setBeforePhoto(
                beforePhotoPath
        );
    }

    if (afterPhoto != null &&
            !afterPhoto.isEmpty()) {

        String afterPhotoPath =
                savePhoto(
                        afterPhoto,
                        "engineer/after"
                );

        assignment.setAfterPhoto(
                afterPhotoPath
        );
    }

    if (status == null ||
            status.isBlank()) {

        throw new RuntimeException(
                "Status is required"
        );
    }

    String newStatus =
            status.trim().toUpperCase();

    if ("INSPECTION_COMPLETED"
            .equals(newStatus)) {

        if (assignment
                .getInspectionCompletedAt()
                == null) {

            assignment
                    .setInspectionCompletedAt(
                            LocalDateTime.now()
                    );
        }
    }

    else if ("IN-PROGRESS"
            .equals(newStatus)) {

        if (assignment
                .getInProgressAt()
                == null) {

            assignment
                    .setInProgressAt(
                            LocalDateTime.now()
                    );
        }
    }

    else if ("COMPLETED"
            .equals(newStatus)) {

        if (assignment
                .getCompletedAt()
                == null) {

            assignment
                    .setCompletedAt(
                            LocalDateTime.now()
                    );
        }
    }

    else {

        throw new RuntimeException(
                "Invalid Task Status"
        );
    }

    assignment.setStatus(
            newStatus
    );

    report.setStatus(
            newStatus
    );

    report.calculatePriority();

    reportRepo.save(report);

    Assignment savedAssignment =
            assignRepo.save(
                    assignment
            );

    updateEngineerStatus(
            assignment.getEngineer()
    );

    return savedAssignment;
}   

private String savePhoto(MultipartFile photo,String folderName) {

    if (photo == null || photo.isEmpty()) {
        return null;
    }

    try {

        String uploadDir = "uploads/" + folderName + "/";

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis()+ "_"+ photo.getOriginalFilename();

        Path path =Paths.get(uploadDir,fileName);

        Files.copy(photo.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/"+ folderName+ "/"+ fileName;

    } catch (IOException e) {

        throw new RuntimeException("Image upload failed", e);
    }
}
}
