package com.project.projectpothels.controlers.Citizen;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.projectpothels.model.RegisModel;
import com.project.projectpothels.model.Admin.RepairReview;
import com.project.projectpothels.model.Admin.RoadReport;
import com.project.projectpothels.service.Citizen.CitizenService;

@RestController
@RequestMapping("/api/citizen")
public class CitizenController {

    @Autowired
    private CitizenService citiserv;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam String email) {
        Map<String, Object> data = new LinkedHashMap();

        RegisModel model = citiserv.getCitizen(email);

        data.put("citizen", model);

        data.put("totalReports", citiserv.totalReport(email));

        data.put("pendingReports", citiserv.pendingReport(email));

        data.put("inProgressReports", citiserv.inProgressReport(email));

        data.put("completedReports", citiserv.completedReport(email));

        data.put("reports", citiserv.getMyReports(email));

        return data;
    }

    @GetMapping("/reports")
    public List<RoadReport> getMyReport(@RequestParam String email) {
        return citiserv.getMyReports(email);
    }

    @GetMapping("/reports/{id}")
    public RoadReport getReport(@PathVariable Long id, @RequestParam String email) {
        return citiserv.getMyReport(id, email);
    }

    @PostMapping(value = "/report", consumes = "multipart/form-data")
    public RoadReport createReport(

            @RequestParam String citizenName,

            @RequestParam String citizenEmail,

            @RequestParam String area,

            @RequestParam String roadName,

            @RequestParam(required = false) String landmark,

            @RequestParam String problemType,

            @RequestParam String severity,

            @RequestParam String description,

            @RequestParam String citizenPhone,

            @RequestParam(required = false) Double latitude,

            @RequestParam(required = false) Double longitude,

            @RequestParam(value = "photos", required = false) List<MultipartFile> photos) {

        RoadReport report = new RoadReport();

        report.setCitizenName(citizenName);

        report.setCitizenEmail(citizenEmail);

        report.setArea(area);

        report.setRoadName(roadName);

        report.setLandmark(landmark);

        report.setProblemType(problemType);

        report.setSeverity(severity);

        report.setDescription(description);

        report.setCitizenPhone(citizenPhone);

        report.setLatitude(latitude);

        report.setLongitude(longitude);

        RoadReport savedReport =
            citiserv.createReport(report);

        citiserv.saveReportPhotos(
            savedReport,
            photos
        );

    return savedReport;
    }

    @GetMapping("/report/{id}/track")
    public Map<String, Object> trackReport(@PathVariable Long id,@RequestParam String email){
        return citiserv.getTrackingDetails(id, email);
    }

    @PostMapping("/review/{assignmentId}")
    public RepairReview submitReview(@PathVariable Long assignmentId,@RequestBody Map<String,Object> request){
        String email = String.valueOf(request.get("email"));
        Integer rating = Integer.valueOf(String.valueOf(request.get("rating")));

        String review = request.get("review") == null ? "" : String.valueOf(request.get("review"));
        return citiserv.submitReview(assignmentId, email, rating, review);

    }
    
}
