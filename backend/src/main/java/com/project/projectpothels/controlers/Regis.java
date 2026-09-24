package com.project.projectpothels.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Regis {
    
    @GetMapping("/register")
    public String registerPage() {
        return "Login/register";
    }
}
