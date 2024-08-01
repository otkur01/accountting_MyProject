package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.User;
import com.cydeo.entity.UserPrincipal;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
//import org.springframework.security.core.context.SecurityContextHolder;
import com.cydeo.util.MapperUtil;
import org.springframework.security.core.userdetails.UserDetails;
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

    public CompanyServiceImpl(UserService userService, CompanyRepository companyRepository, SecurityService securityService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Long getCompanyIdByLoggedInUser(UserDto user) {
        //String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDto loggedInUser = userService.findByUserName(user.getUsername());

        return loggedInUser.getCompany().getId();



    }

    @Override
    public List<CompanyDto> getAllCompany() {
        return companyRepository.findAll().stream().map(company -> mapperUtil.convert(company,new CompanyDto())).collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyByTitle(String title) {
        return mapperUtil.convert(companyRepository.findByTitle(title),new CompanyDto());
    }

    @Override
    public CompanyDto getById(Long id) {
        return mapperUtil.convert(companyRepository.findById(id).get(), new CompanyDto());
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

    @Override
    public void activeCompanyStatus(Long id) {
        Company company= companyRepository.findById(id).orElseThrow();
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }

    @Override
    public void deactivateCompanyStatus(Long id) {
        Company company= companyRepository.findById(id).orElseThrow();
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }

    public boolean isTitleUnique(String title) {
        return companyRepository.findByTitle(title)==null;
    }

//    @Override
//    public boolean isTitleUniqueForUpdate(String title) {
//        List<Company>companyList = companyRepository.findAllByTitleIsNot(title).stream().filter(
//                company -> company.getTitle().equals(title)
//        ).collect(Collectors.toList());
//        return companyList.
//
//
//    }
}
