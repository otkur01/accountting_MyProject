package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;

import java.util.List;

public interface CompanyService {

  Long getCompanyIdByLoggedInUser(UserDto user);

  List<CompanyDto> getAllCompany();

  CompanyDto getCompanyByTitle(String title);

  CompanyDto getById(Long id);

  List<CompanyDto> getAllCompaniesByIdNotEqual(Long id);

  CompanyDto save(CompanyDto companyDto);

    CompanyDto update(CompanyDto companyDto);

  void activeCompanyStatus(Long id);

  void deactivateCompanyStatus(Long id);

  boolean isTitleUnique(String title);
 // boolean isTitleUniqueForUpdate(String title);


}
