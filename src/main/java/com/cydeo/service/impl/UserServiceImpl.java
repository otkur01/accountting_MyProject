package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    private final CompanyService companyService;

    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy SecurityService securityService, @Lazy CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    @Override
    public UserDto findByUserName(String userName) {
        User user = userRepository.findByUsernameAndIsDeleted(userName,false);

        return mapperUtil.convert(user, new UserDto());

    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAllByIsDeleted(false).stream().map(user -> mapperUtil.convert(user,new UserDto())).collect(Collectors.toList());
    }



    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserDto userDto = mapperUtil.convert(user, new UserDto());
        userDto.setOnlyAdmin(checkIftheAdminIsOnly(id));



        return userDto;
    }

   public boolean checkIftheAdminIsOnly(Long id) {

       List<UserDto>userDtoList = getAllUserInCompany(getCompanyIdByUserId(id).getId()).stream().filter(userDto -> userDto.getRole().getDescription().equals("Admin")).collect(Collectors.toList());
      return userDtoList.size()==1;
    }

    @Override
    public Long getCompanyIdByUsername(String username) {
        return findByUsername(username).getId();
    }

    @Override
    public CompanyDto getCompanyIdByUserId(Long id) {
        return mapperUtil.convert(userRepository.findById(id).get().getCompany(), new CompanyDto());
    }

    @Override
    public List<UserDto> getAllUserOrderedByCompanyAndRole() {
        return userRepository.findAllByOrderByCompanyTitleAscRoleAsc().stream().map(user -> mapperUtil.convert(user,new UserDto())).collect(Collectors.toList());
    }

    @Override
    public UserDto findByUsername(String currentUsername) {
        return mapperUtil.convert(userRepository.findByUsername(currentUsername).orElseThrow(), new UserDto());
    }



    @Override
    public List<UserDto> getAllAdminUserSorted() {
        return userRepository.findAllByOrderByCompanyTitleAscRoleAsc().stream()
                .filter(user -> user.getIsDeleted()==false)
                .map(user -> mapperUtil.convert(user,new UserDto()))
                .filter(userDto -> userDto.getRole().getDescription().equals("Admin"))
                .map(userDto -> {
                    userDto.setOnlyAdmin(checkIftheAdminIsOnly(userDto.getId()));
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUserInCompany( Long id) {
        return userRepository.findAll().stream()
                .filter(user -> user.getIsDeleted()==false)
                .filter(user -> user.getCompany().getId().equals(id))
                .filter(user -> !user.getId().equals(securityService.getLoggedInUser().getId()))
                .map(user -> mapperUtil.convert(user,new UserDto()))
//
//
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {

        User convertUser =mapperUtil.convert(userDto,new User());
//        convertUser.setCompany(mapperUtil.convert(companyService.getCompanyByTitle(userDto.getCompany().getTitle()), new Company()));
//        convertUser.setRole(mapperUtil.convert(roleService.getRoleByDescription(userDto.getRole().getDescription()), new Role()));


        return mapperUtil.convert(userRepository.save(convertUser),new UserDto());

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        //Find current user
      //  User user1 = userRepository.findByUsernameAndIsDeleted(userDto.getUsername(), false);  //has id
        //Map update user dto to entity object
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        User convertedUser = mapperUtil.convert(userDto,new User());   // has id?
        //set id to the converted object
        convertedUser.setId(userDto.getId());
        convertedUser.setInsertDateTime(user.insertDateTime);
        convertedUser.setInsertUserId(user.insertUserId);
        convertedUser.setEnabled(true);
        //save the updated user in the db
       userRepository.save(convertedUser);
       return findByUsername(convertedUser.getUsername());
    }

    @Override
    public void deleteUser(Long id) {
        if(!checkIftheAdminIsOnly(id)){
        User user = userRepository.findById(id).orElseThrow();
        user.setIsDeleted(true);
        user.setUsername(user.getUsername()+""+user.getId());
        userRepository.save(user);}

    }
}
