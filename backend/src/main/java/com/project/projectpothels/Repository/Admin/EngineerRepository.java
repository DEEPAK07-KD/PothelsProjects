package com.project.projectpothels.Repository.Admin;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.projectpothels.model.Admin.EngineerModel;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;


@Repository
public interface EngineerRepository extends JpaRepository<EngineerModel, Long>{
    long countByStatus(String status);

    List<EngineerModel> findByStatus(String status, org.springframework.data.domain.Sort sort);

    List<EngineerModel> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Sort sort);

     Optional<EngineerModel> findByEmail(String email);
     
}
