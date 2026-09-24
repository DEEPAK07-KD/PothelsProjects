package com.project.projectpothels.controlers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.projectpothels.Repository.RegisRepository;
import com.project.projectpothels.model.RegisModel;

@RestController
@RequestMapping("/api")
public class LoginController {
    
    @Autowired
    RegisRepository repo;
 
    @PostMapping("/login")
    public RegisModel login(@RequestBody RegisModel mo){
        Optional<RegisModel> existingUser = repo.findByEmail(mo.getEmail());

        if (existingUser.isPresent()) {
            RegisModel dbuser = existingUser.get();

            if (dbuser.getPassword().equals(mo.getPassword())) {
                return dbuser;
            }
        }
        return null;
    }
}
