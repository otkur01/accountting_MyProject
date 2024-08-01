package com.cydeo.controller;


import com.cydeo.dto.CompanyDto;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;


    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getAllCompany(Model model){
        model.addAttribute("companies", companyService.getAllCompaniesByIdNotEqual(1L));

        return "company/company-list";
    }

    @GetMapping("/create")
    public String getCreateCompanyForm(Model model){
        model.addAttribute("newCompany", new CompanyDto());

        return "company/company-create";
    }


    @PostMapping("/create")
    public String createNewCompany(@Valid @ModelAttribute("newCompany")CompanyDto companyDto, BindingResult bindingResult, Model model){
        if(!companyService.isTitleUnique(companyDto.getTitle())){
            bindingResult.rejectValue("title", "error.item", "This title already exists.");
            model.addAttribute("newCompany", companyDto);

            return "company/company-create";
        }



        if (bindingResult.hasErrors()){
            model.addAttribute("newCompany", companyDto);

            return "company/company-create";
        }



        companyService.save(companyDto);


     return "redirect:/companies/list";
    }

 @GetMapping("/update/{id}")
    public String GetUpdateCompany(@PathVariable("id")Long id, Model model){
         model.addAttribute("company", companyService.getById(id));

     return "company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@Valid@ModelAttribute("company")CompanyDto companyDto, BindingResult bindingResult, Model model, @PathVariable("id")Long id){
        if(companyDto.getTitle().equals(companyService.getById(id).getTitle())){
            if(bindingResult.hasErrors()){
                model.addAttribute("company", companyDto);
                return   "company/company-update";

            }

        }

        else  {
            if(!companyService.isTitleUnique(companyDto.getTitle())){
                bindingResult.rejectValue("title", "error.item", "This title already exists.");
                model.addAttribute("newCompany", companyDto);

                return "company/company-update";
            }
        }






//     if(bindingResult.hasErrors()){
//         model.addAttribute("company", companyDto);
//         return   "company/company-update";
//
//     }
        companyService.update(companyDto);
        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{id}")
    public String activeCompany(@PathVariable("id")Long id){
        companyService.activeCompanyStatus(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id")Long id){
        companyService.deactivateCompanyStatus(id);
        return "redirect:/companies/list";
    }


}
