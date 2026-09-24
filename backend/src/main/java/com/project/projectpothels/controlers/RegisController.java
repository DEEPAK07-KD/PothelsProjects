package com.project.projectpothels.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.projectpothels.model.RegisModel;
import com.project.projectpothels.service.RegisService;

@RestController
@RequestMapping("/api")
public class RegisController {
    
    @Autowired
    RegisService se;

    @PostMapping("/register")
    public RegisModel re(@RequestBody RegisModel mo){
        return se.regis(mo);
    }
}
