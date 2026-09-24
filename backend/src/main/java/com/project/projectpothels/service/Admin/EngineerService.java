package com.project.projectpothels.service.Admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.projectpothels.Repository.Admin.EngineerRepository;
import com.project.projectpothels.Repository.Admin.RepairReviewRepository;
import com.project.projectpothels.model.Admin.EngineerModel;
import com.project.projectpothels.model.Admin.RepairReview;

@Service
public class EngineerService {
    
    @Autowired
    private RepairReviewRepository reviewrepo;

    @Autowired
    private EngineerRepository repository;

    public List<EngineerModel> getAllEngineers(){
        return repository.findAll(
            Sort.by("engineerCode").ascending()
        );

    }

    public EngineerModel getEngineer(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Engineer not Found"));
    }

    public EngineerModel saveEngineer(EngineerModel engineer){
        if (engineer.getStatus() == null || engineer.getStatus().isBlank()) {
            engineer.setStatus("AVAILABLE");
        }

        return repository.save(engineer);
    }

    public EngineerModel updateEngineer(Long id,EngineerModel data){
        EngineerModel engineer = getEngineer(id);

        engineer.setEngineerCode(data.getEngineerCode());
        engineer.setName(data.getName());
        engineer.setPhone(data.getPhone());
        engineer.setEmail(data.getEmail());
        engineer.setSpecialization(data.getSpecialization());
        engineer.setStatus(data.getStatus());
        
        if (data.getPassword() != null && !data.getPassword().isBlank()) {
            engineer.setPassword(data.getPassword());
        }

        return repository.save(engineer);
    }

    public void deleteEngineer(Long id){
        repository.deleteById(id);
    }

    public List<EngineerModel> searchEngineers(String search){
        return repository.findByNameContainingIgnoreCase(
            search,
            Sort.by("engineerCode").ascending()
        );
    }

    

    public List<EngineerModel> getByStatus(String status){
        return repository.findByStatus(
            status,
            Sort.by("engineerCode").ascending()
        );
    }

    public long totalEngineers(){
        return repository.count();
    }

    public long availableEngineer(){
        return repository.countByStatus("AVAILABLE");
    }

    public long assignEngineer(){
        return repository.countByStatus("ASSIGNED");
    }

    public long onLeaveEngineers(){
        return repository.countByStatus("ON_LEAVE");
    }

    public Map<String, Object> getEngineerReviewDetails(Long engineerId){
        EngineerModel engineer = getEngineer(engineerId);
        List<RepairReview> reviews = reviewrepo.findByAssignment_Engineer_Id(engineerId);
        double averageRataing = 0.0;
        if (!reviews.isEmpty()) {
            averageRataing = reviews.stream().mapToInt(RepairReview::getRating).average().orElse(0.0);
        }
        int performanceScore = calculateEngineerPerformance(averageRataing);
        Map<String, Object> data = new  LinkedHashMap<>();
        data.put("engineerId", engineer.getId());
        data.put("engineerCode", engineer.getEngineerCode());
        data.put("engineerName", engineer.getName());
        data.put("averageRating", averageRataing);
        data.put("totalReviews", reviews.size());
        data.put("performanceScore", performanceScore);
        data.put("reviews", reviews);

        return data;
    }

    private int calculateEngineerPerformance(double averageRating){
        if (averageRating >= 4.5) {
            return 100;
        }
        if (averageRating >= 4.0) {
            return 90;
        }
        if (averageRating >= 3.5) {
            return 80;
        }
        if (averageRating >= 3.0) {
            return 70;
        }
        if (averageRating >= 2.0) {
            return 50;
        }
        if (averageRating > 0) {
            return 30;
        }   
        return 0;
    }
}
