package com.cydeo.controller;

import com.cydeo.dto.ProductDto;
import com.cydeo.enums.ProductUnit;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private  final CategoryService categoryService;




    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        return "/product/product-list";


    }
          @GetMapping("/create")
          public String getNewProduct(Model model){



          model.addAttribute("newProduct", new ProductDto());
          model.addAttribute("categories",categoryService.getAllCategories());
          model.addAttribute("productUnits", productService.getAllProductUnits());
          return "/product/product-create";
          }

      @PostMapping("/create")
     public String createNewProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model){
       if(bindingResult.hasErrors()){

           model.addAttribute("newProduct", productDto);
           model.addAttribute("categories",categoryService.getAllCategories());
           model.addAttribute("productUnits", productService.getAllProductUnits());
           return "/product/product-create";
       }

        productService.save(productDto);

        return "redirect:/products/list";
      }

      @GetMapping("/update/{id}")
      public String getUpdateForm(@PathVariable("id")Long id,  Model model){


          model.addAttribute("product", productService.getById(id));
          model.addAttribute("categories",categoryService.getAllCategories());
          model.addAttribute("productUnits", productService.getAllProductUnits());
          return "/product/product-update";
      }

      @PostMapping("/update/{id}")
    public String updateProduct(@ModelAttribute("product") ProductDto productDto){
        productService.update(productDto);


          return "redirect:/products/list";
      }


      @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id")Long id){
        productService.delete(id);
          return "redirect:/products/list";
      }


}
