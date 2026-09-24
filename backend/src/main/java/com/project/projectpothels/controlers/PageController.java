package com.project.projectpothels.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/Admin/AdminDashboard")
    public String adminDashboard() {
        return "Admin/AdminDashboard";
    }

    @GetMapping("/Admin/Engineer")
    public String engineer() {
        return "Admin/Engineer";
    }

    @GetMapping("/Admin/report")
    public String report() {
        return "Admin/report";
    }

    @GetMapping("/Admin/priority")
    public String priority() {
        return "Admin/priority";
    }

    @GetMapping("/Admin/assignment")
    public String assignment() {
        return "Admin/assignment";
    }

    @GetMapping("/Admin/analytics")
    public String analytics() {
        return "Admin/analytics";
    }

    @GetMapping("/Engineer/dashboard")
    public String engineerDashboard() {
        return "Engineer/EngineerDashboard";
    }

    @GetMapping("/Engineer/repair")
    public String engineerRepair() {
        return "Engineer/repair";
    }

    @GetMapping("/Citizen/CitizenDashboard")
    public String citizenDashboard() {
        return "Citizen/CitizenDashboard";
    }

    @GetMapping("/Citizen/report")
    public String citizenReport() {
        return "Citizen/Report";
    }

    @GetMapping("/Citizen/track")
    public String citizenTrack() {
        return "Citizen/Track";
    }
}
