package com.project.projectpothels.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.projectpothels.model.RegisModel;

@Repository
public interface RegisRepository extends JpaRepository<RegisModel,Long>{
    
     Optional<RegisModel> findByEmail(String email);
     
}
