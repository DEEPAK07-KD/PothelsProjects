package com.project.projectpothels.controlers.Admin;

public record EngineerRequest(
     String engineerCode,
     String name,
     String phone,
     String email,
    String specialization,
    String status,
    String password    
) {
} 
