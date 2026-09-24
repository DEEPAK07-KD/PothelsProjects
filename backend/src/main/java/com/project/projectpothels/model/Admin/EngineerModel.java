package com.project.projectpothels.model.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "engineer")
public class EngineerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String engineerCode;

    private String name,phone,email,specialization,status,password,beforePhoto,afterPhoto,inspectionNotes;

    public EngineerModel() {
    }

    public EngineerModel(Long id, String engineerCode, String name, String phone, String email, String specialization,
            String status,String password,String beforePhoto, String afterPhoto, String inspectionNotes) {
        this.id = id;
        this.engineerCode = engineerCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.status = status;
        this.password = password;
        this.beforePhoto = beforePhoto;
        this.afterPhoto = afterPhoto;
        this.inspectionNotes = inspectionNotes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEngineerCode() {
        return engineerCode;
    }

    public void setEngineerCode(String engineerCode) {
        this.engineerCode = engineerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeforePhoto() {
        return beforePhoto;
    }

    public void setBeforePhoto(String beforePhoto) {
        this.beforePhoto = beforePhoto;
    }

    public String getAfterPhoto() {
        return afterPhoto;
    }

    public void setAfterPhoto(String afterPhoto) {
        this.afterPhoto = afterPhoto;
    }

    public String getInspectionNotes() {
        return inspectionNotes;
    }

    public void setInspectionNotes(String inspectionNotes) {
        this.inspectionNotes = inspectionNotes;
    }

    
    
}
