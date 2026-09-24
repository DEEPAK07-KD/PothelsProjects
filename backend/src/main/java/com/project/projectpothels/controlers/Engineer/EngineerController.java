package com.project.projectpothels.controlers.Engineer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.projectpothels.Repository.Admin.EngineerRepository;
import com.project.projectpothels.model.Admin.Assignment;
import com.project.projectpothels.model.Admin.EngineerModel;
import com.project.projectpothels.service.Admin.AssignmentService;

@RestController
@RequestMapping("/api/engineer")
public class EngineerController {
    
    @Autowired
    private EngineerRepository engrepo;

    @Autowired
    private AssignmentService assignservice;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam String email){
        EngineerModel engimodel = engrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Engineer Not Found"));

    List<Assignment> assignment=assignservice.getAssignmentsByEngineer(email);

    long assigned = assignment.size();

    long inprogress = assignment.stream().filter(a -> "IN-PROGRESS".equalsIgnoreCase(a.getStatus())).count();

    long completed = assignment.stream().filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus())).count();

    Map<String, Object> data = new LinkedHashMap<>();

    data.put("engineer", engimodel);
    data.put("assigned", assigned);
    data.put("inProgress", inprogress);
    data.put("completed", completed);

    return data;
    }

    @GetMapping("/tasks")
    public List<Map<String, Object>> task(@RequestParam String email){
        List<Assignment> assignments = assignservice.getAssignmentsByEngineer(email);
        return assignments.stream().map(a -> {
            Map<String, Object> task = new LinkedHashMap<>();

            task.put("assignmentId", a.getId());
            task.put("reportId", a.getReport().getId());
            task.put("reportCode", a.getReport().getReportCode());
            task.put("area", a.getReport().getArea());
            task.put("roadName", a.getReport().getRoadName());
            task.put("problemType", a.getReport().getProblemType());
            task.put("severity", a.getReport().getSeverity());
            task.put("priorityScore", a.getReport().getPriorityScore());
            task.put("status",a.getStatus());
            task.put("beforePhoto", a.getBeforePhoto());
            task.put("afterPhoto", a.getAfterPhoto());
            return task;
        })
        .toList();
    }

    @GetMapping("/task/{assignmentId}")
    public Map<String,Object> getTask(@PathVariable Long assignmentId, @RequestParam String email){
        Assignment a = assignservice.getAssignment(assignmentId,email);
        Map<String, Object> task = new LinkedHashMap<>();

        task.put("assignmentId", a.getId());
        task.put("reportCode", a.getReport().getReportCode());
        task.put("area", a.getReport().getArea());
        task.put("roadName", a.getReport().getRoadName());
        task.put("problemType", a.getReport().getProblemType());
        task.put("severity", a.getReport().getSeverity());
        task.put("priorityScore", a.getReport().getPriorityScore());
        task.put("status", a.getStatus());

        return task;
    }

    @PutMapping(value = "/task/{assignmentId}",consumes = "multipart/form-data")
    public Assignment updateTask(
        @PathVariable Long assignmentId,

        @RequestParam String email,

        @RequestParam String severity,

        @RequestParam String status,

        @RequestParam String notes,

        @RequestPart(value = "beforePhoto",required = false) MultipartFile beforePhoto,

        @RequestPart(value = "afterPhoto",required = false) MultipartFile afterPhoto) {

    return assignservice.updateTask(
        assignmentId,
        email,
        severity,
        status,
        notes,
        beforePhoto,
        afterPhoto
    );
}
}
