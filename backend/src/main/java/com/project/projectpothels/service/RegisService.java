package com.project.projectpothels.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.projectpothels.Repository.RegisRepository;
import com.project.projectpothels.model.RegisModel;

@Service
public class RegisService {
    @Autowired
    RegisRepository repo;

    public RegisModel regis(RegisModel mo){
        return repo.save(mo);    
    }
}
