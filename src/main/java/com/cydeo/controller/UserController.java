package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    private final SecurityService securityService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService, SecurityService securityService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String getAllUsers(Model model){
        String role = securityService.getLoggedInUser().getRole().getDescription();

        if(role.equals("Root User")){
            model.addAttribute("users",userService.getAllAdminUserSorted());
            return "user/user-list";
        }

        else if(role.equals("Admin")) {
            model.addAttribute("users",userService.getAllUserInCompany(securityService.getLoggedInUser().getId()));
            return "user/user-list";
        }
//        model.addAttribute("users",userService.getAllUserOrderedByCompanyAndRole());
//
        return "user/user-list";
    }

    @GetMapping("/create")
    public String createNewUser(Model model){
        model.addAttribute("newUser", new UserDto());

        String role = securityService.getLoggedInUser().getRole().getDescription();

        if(role.equals("Root User")){

            model.addAttribute("userRoles",roleService.getAllRoles().stream()
                    .filter(roleDto -> roleDto.getDescription().equals("Admin")).collect(Collectors.toList()));
            model.addAttribute("companies", companyService.getAllCompany()
                    .stream().filter(companyDto -> !companyDto.getTitle().equals("CYDEO")).collect(Collectors.toList()));


        }

       if(role.equals("Admin")) {
            model.addAttribute("userRoles",roleService.getAllRoles().stream()
                   .filter(roleDto -> ! roleDto.getDescription().equals("Root User")).collect(Collectors.toList()));                    ;
           model.addAttribute("companies", companyService.getAllCompany()
                   .stream().filter(companyDto -> companyDto.getTitle().equals(securityService.getLoggedInUser().getCompany().getTitle())).collect(Collectors.toList()));
        }




        return "/user/user-create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {

            return "redirect:/users/create";
        }

        userService.save(userDto);
        return "redirect:/users/list";
    }

 @GetMapping("/update/{id}")
    public String getUpdateUser(@PathVariable("id")Long id, Model model){
        UserDto userDto = userService.getById(id);

          model.addAttribute("user", userDto);
          model.addAttribute("userRoles", roleService.getAllRoles());
          model.addAttribute("companies", companyService.getAllCompany());


     return "/user/user-update";
 }

 @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("newUser")UserDto userDto){
        userService.updateUser(userDto);
     return "redirect:/users/list";
 }

 @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id, Model model){
     UserDto user = userService.getById(id);

     model.addAttribute("user", user);
     userService.deleteUser(id);
   return "redirect:/users/list";
 }



}
