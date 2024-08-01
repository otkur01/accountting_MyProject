package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUserName(String username);

    List<UserDto> getAllUsers();

    UserDto getById(Long id);

    Long getCompanyIdByUsername(String username);

    CompanyDto getCompanyIdByUserId(Long id);

     List<UserDto> getAllUserOrderedByCompanyAndRole();
    UserDto findByUsername(String currentUsername);

    boolean checkIftheAdminIsOnly(Long id);



    List<UserDto> getAllAdminUserSorted();

    List<UserDto>getAllUserInCompany(Long id);

   UserDto save(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Long id);
}
