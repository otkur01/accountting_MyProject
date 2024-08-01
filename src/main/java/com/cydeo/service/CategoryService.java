package com.cydeo.service;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CategoryService {

    CategoryDto getByDescription(String description);
    List<CategoryDto> getAllCategories();

   List<CategoryDto> getAllCategoriesOrderedByDescriptionAcs(CompanyDto companyDto);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);
}
