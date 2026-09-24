package com.project.projectpothels.controlers.Admin;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.project.projectpothels.model.RegisModel;
import com.project.projectpothels.Repository.RegisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.projectpothels.model.Admin.EngineerModel;
import com.project.projectpothels.model.Admin.RoadReport;
import com.project.projectpothels.service.Admin.AnalyticsService;
import com.project.projectpothels.service.Admin.AssignmentService;
import com.project.projectpothels.service.Admin.EngineerService;
import com.project.projectpothels.service.Admin.RoadReportService;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired 
    private EngineerService engineerService;

    @Autowired 
    private RoadReportService reportService;

    @Autowired 
    private AssignmentService assignmentService;

    @Autowired 
    private AnalyticsService analyticsService;

    @Autowired
    private RegisRepository regisRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalReports", reportService.totalReports());
        data.put("newReports", reportService.pendingReports());
        data.put("criticalReports", reportService.criticalReports());
        data.put("completedRepairs", reportService.completedReports());
        data.put("priorityReports", reportService.getPriorityReports().stream().limit(5).toList());
        data.put("recentReports", reportService.getAllReports().stream().limit(5).toList());
        data.put("pending", reportService.pendingReports());
        data.put("inProgress", reportService.inProgressReports());
        data.put("completed", reportService.completedReports());
        return data;
    }

    @GetMapping("/engineers")
    public List<EngineerModel> engineers(@RequestParam(required = false) String search,
                                         @RequestParam(required = false) String status) {
        if (search != null && !search.isBlank()) {
            return engineerService.searchEngineers(search);
        }
        if (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) {
            return engineerService.getByStatus(status);
        }
        return engineerService.getAllEngineers();
    }

    @GetMapping("/engineers/stats")
    public Map<String, Long> engineerStats() {
        Map<String, Long> data = new LinkedHashMap<>();
        data.put("total", engineerService.totalEngineers());
        data.put("available", engineerService.availableEngineer());
        data.put("assigned", engineerService.assignEngineer());
        data.put("onLeave", engineerService.onLeaveEngineers());
        return data;
    }

    @PostMapping("/engineers")
    public EngineerModel addEngineer(@RequestBody EngineerRequest request) {
        EngineerModel eng = new EngineerModel();
            eng.setEngineerCode(request.engineerCode());
            eng.setName(request.name());
            eng.setPhone(request.phone());
            eng.setEmail(request.email());
            eng.setSpecialization(request.specialization());
            eng.setStatus(request.status());
            eng.setPassword(request.password());

            EngineerModel savedEngineer = engineerService.saveEngineer(eng);
            RegisModel loginUser = new RegisModel();

            loginUser.setName(request.name());
            loginUser.setUsername(request.engineerCode());
            loginUser.setEmail(request.email());
            loginUser.setPassword(request.password());
            loginUser.setRole("ENGINEER");

            regisRepository.save(loginUser);

            return savedEngineer;        
    }

    @PutMapping("/engineers/{id}")
    public EngineerModel updateEngineer(@PathVariable Long id, @RequestBody EngineerModel engineer) {
        return engineerService.updateEngineer(id, engineer);
    }

    @DeleteMapping("/engineers/{id}")
    public Map<String, String> deleteEngineer(@PathVariable Long id) {
        engineerService.deleteEngineer(id);
        return Map.of("message", "Engineer deleted successfully");
    }

    @GetMapping("/reports")
    public List<RoadReport> reports(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return reportService.searchReports(search);
        }
        return reportService.getAllReports();
    }

    @GetMapping("/reports/stats")
    public Map<String, Long> reportStats() {
        Map<String, Long> data = new LinkedHashMap<>();
        data.put("pending", reportService.pendingReports());
        data.put("verified", reportService.verifiedReports());
        data.put("rejected", reportService.rejectedReports());
        return data;
    }

    @PutMapping("/reports/{id}/verify")
    public Map<String, String> verifyReport(@PathVariable Long id) {
        reportService.verifyReport(id);
        return Map.of("message", "Report verified successfully");
    }

    @PutMapping("/reports/{id}/reject")
    public Map<String, String> rejectReport(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        reportService.rejectReport(id,reason);
        return Map.of("message", "Report rejected successfully");
    }

    @GetMapping("/priority")
    public List<RoadReport> priority(@RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return reportService.searchPriorityReports(search);
        }
        return reportService.getPriorityReports();
    }

    @GetMapping("/priority/stats")
    public Map<String, Long> priorityStats() {
        Map<String, Long> data = new LinkedHashMap<>();
        data.put("critical", reportService.criticalPriority());
        data.put("high", reportService.highPriority());
        data.put("medium", reportService.mediumPriority());
        data.put("low", reportService.lowPriority());

        return data;
    }

    @GetMapping("/assignments")
    public Map<String, Object> assignments() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reports", reportService.getPriorityReports());
        data.put("engineers", engineerService.getAllEngineers().stream().filter(e -> !"ON_LEAVE".equalsIgnoreCase(e.getStatus())).toList());
        data.put("assignments", assignmentService.getAllAssignments());
        return data;
    }

    @PostMapping("/assignments")
    public Object createAssignment(@RequestBody AssignmentRequest request) {
        return assignmentService.assignReport(request.reportId(), request.engineerId(),
                LocalDate.parse(request.assignedDate()));
    }

    @GetMapping("/analytics")
    public Map<String, Object> analytics() {
        return analyticsService.getAnalytics();
    }

    public record AssignmentRequest(Long reportId, Long engineerId, String assignedDate) {}

    @GetMapping("/engineers/{id}/reviews")
    public Map<String, Object> engineerReviews(@PathVariable Long id){
        return engineerService.getEngineerReviewDetails(id);
    }
}
