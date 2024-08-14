package com.cydeo.controller;


import com.cydeo.dto.CategoryDto;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    private  final SecurityService securityService;

    private final ProductService productService;

    public CategoryController(CategoryService categoryService, SecurityService securityService, ProductService productService) {
        this.categoryService = categoryService;
        this.securityService = securityService;
        this.productService = productService;
    }


    @GetMapping("/list")
    public String getAllCategories(Model model){
        model.addAttribute("categories", categoryService.getAllCategoriesOrderedByDescriptionAcs(securityService.getLoggedInUser().getCompany()));
        return "category/category-list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model){
        model.addAttribute("newCategory", new CategoryDto());

        return "category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("newCategory")CategoryDto categoryDto, BindingResult bindingResult, Model model){


       if (bindingResult.hasErrors()){
           model.addAttribute("newCategory", categoryDto);
           return "category/category-create";
       }
       if (categoryService.listOfCategoryDescription().contains(categoryDto.getDescription())){
           bindingResult.rejectValue("description", "description.item", "This description already exists.");
           model.addAttribute("newCategory", categoryDto);
           return "category/category-create";
       }

        categoryService.save(categoryDto);



        return "redirect:/categories/list";
    }


    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id")Long id, Model model){
       CategoryDto categoryDto = categoryService.getById(id);

        if(!(productService.getAllProductByCategoryId(id).isEmpty())){
           categoryDto.setHasProduct(true);
        }
     model.addAttribute("category", categoryDto);
     return "category/category-update";

    }


    @PostMapping("/update/{id}")
    public String updateCategory(@Valid@ModelAttribute("category")CategoryDto categoryDto, BindingResult bindingResult, Model model, @PathVariable("id")Long id){

       if (bindingResult.hasErrors()){
           model.addAttribute("category", categoryDto);
           return "category/category-update";
       }

        if (categoryService.listOfCategoryDescription().contains(categoryDto.getDescription())){
            bindingResult.rejectValue("description", "description.item", "This description already exists.");
            model.addAttribute("category", categoryDto);
            return "category/category-update";
        }

        categoryService.update(categoryDto);
        return  "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id")Long id, Model model){
         CategoryDto  categoryDto = categoryService.getById(id);
         if(!productService.getAllProductByCategoryId(id).isEmpty()) {
             categoryDto.setHasProduct(true);
             model.addAttribute("newCategory", new CategoryDto());
         }
       else{ categoryService.delete(id);}
        return  "redirect:/categories/list";
    }
}
