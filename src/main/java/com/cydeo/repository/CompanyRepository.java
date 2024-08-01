package com.cydeo.repository;

import com.cydeo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  Company findByTitle(String title);
  List<Company> findByIdNot(Long id);


  List<Company> findAllByTitleIsNot(String title);
}
