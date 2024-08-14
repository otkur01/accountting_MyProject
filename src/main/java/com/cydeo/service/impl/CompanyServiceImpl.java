package com.cydeo.service.impl;

import com.cydeo.annotation.ActiveDeActive;
import com.cydeo.client.CountryClient;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.CountryDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.execption.CompanyNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
//import org.springframework.security.core.context.SecurityContextHolder;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final SecurityService securityService;

    private final MapperUtil mapperUtil;

    private final CountryClient client;

    public CompanyServiceImpl(UserService userService, CompanyRepository companyRepository, SecurityService securityService, MapperUtil mapperUtil, CountryClient client) {
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
        this.client = client;
    }

//    @Override
//    public Long getCompanyIdByLoggedInUser(UserDto user) {
//        //String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        UserDto loggedInUser = userService.findByUserName(user.getUsername());
//
//        return loggedInUser.getCompany().getId();
//
//
//
//    }

    @Override
    public List<CompanyDto> getAllCompany() {
        return companyRepository.findAll().stream().map(company -> mapperUtil.convert(company,new CompanyDto())).collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyByTitle(String title) {
        if(title==null ||  title==""){
            throw new CompanyNotFoundException("Company not found with title: " + title);
        }
        return mapperUtil.convert(companyRepository.findByTitle(title),new CompanyDto());
    }

    @Override
    public CompanyDto getById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));
        return mapperUtil.convert(company, new CompanyDto());
    }

    public CompanyDto findById(Long id){
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with id: " + id));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public List<CompanyDto> getAllCompaniesByIdNotEqual(Long id) {
        return companyRepository.findByIdNot(id).stream().map(company -> mapperUtil.convert(company,new CompanyDto())).sorted(Comparator.comparing(CompanyDto::getCompanyStatus)).collect(Collectors.toList());
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(companyDto,new Company());
        companyRepository.save(company);

        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto update(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.ACTIVE);
        Company companyConverted = mapperUtil.convert(companyDto, new Company());
        companyRepository.save(companyConverted);

        return  mapperUtil.convert(companyConverted, new CompanyDto());
    }


    @ActiveDeActive
    @Override
    public CompanyDto activeCompanyStatus(Long id) {
        Company company= companyRepository.findById(id).orElseThrow();
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);

        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public List<String> nameAllCountry() {
     List<CountryDto>countryDtoList = client.getAllCountries("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJhYmR1bGhla2VtQGdtYWlsLmNvbSIsImFwaV90b2tlbiI6IjBGbVpKVHpndEoyWWl3eXhmUjdKWGJEbTdtdDFnaUtubGxFZGtlZmg2VGVwS3U4NzRYWklPend5blRzSlJUX0JuazgifSwiZXhwIjoxNzIzNjUyMzY4fQ.nVggCeBro1-VWBlcDQbbtV3NDLsspfHgECDcArrctgM");

        return countryDtoList.stream().map(CountryDto::getCountryName).collect(Collectors.toList());
    }

    @ActiveDeActive
    @Override
    public CompanyDto deactivateCompanyStatus(Long id) {
        Company company= companyRepository.findById(id).orElseThrow();
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
        return mapperUtil.convert(company, new CompanyDto());
    }

    public boolean isTitleUnique(String title) {
        return companyRepository.findByTitle(title)==null;
    }


}
