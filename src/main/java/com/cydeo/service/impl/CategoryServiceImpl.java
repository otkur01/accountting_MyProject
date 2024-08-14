package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, SecurityService securityService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.productService = productService;
    }

    @Override
    public CategoryDto getByDescription(String description) {
        return mapperUtil.convert(categoryRepository.findByDescription(description),new CategoryDto());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(category -> mapperUtil.convert(category,new CategoryDto())).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllCategoriesOrderedByDescriptionAcs(CompanyDto companyDto) {
        Company company = mapperUtil.convert(companyDto, new Company());
        List<CategoryDto>categoryDtoList = categoryRepository.findAllByCompanyOrderByDescriptionAsc(company)
                .stream().filter(category -> category.getIsDeleted().equals(false))
                .map(category -> mapperUtil.convert(category, new CategoryDto()))
                .map(categoryDto -> {
                    List<ProductDto> productDtos = productService.getAllProductByCategoryId(categoryDto.getId());
                    categoryDto.setHasProduct(!productDtos.isEmpty());
                    return categoryDto;
                })
                .collect(Collectors.toList());
        return categoryDtoList;
    }

    @Override
    public CategoryDto getById(Long id) {
        return mapperUtil.convert(categoryRepository.findById(id).get(), new CategoryDto());
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {

        categoryDto.setCompany(securityService.getLoggedInUser().getCompany());
        Category category = mapperUtil.convert(categoryDto, new Category());
        if(!checkIfDescriptionExits(categoryDto.getDescription())){
            categoryRepository.save(category);}
        else {
            categoryRepository.save(mapperUtil.convert(getByDescription(categoryDto.getDescription()), new Category()));
        }

        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
     //  Category converted = mapperUtil.convert(categoryDto, new Category());
      Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow();
//       converted.setId(category.getId());
//       converted.setCompany(category.getCompany());
//       converted.setInsertUserId(category.getInsertUserId());
//       converted.setInsertDateTime(category.getInsertDateTime());
    category.setDescription(categoryDto.getDescription());
        categoryRepository.save(category);
        return mapperUtil.convert(category,new CategoryDto());
    }

    @Override
    public void delete(Long id) {
         Category category = categoryRepository.findById(id).orElseThrow();
         category.setIsDeleted(true);
         categoryRepository.save(category);


    }

    @Override
    public List<String> listOfCategoryDescription() {
        return getAllCategories().stream().map(CategoryDto::getDescription).collect(Collectors.toList());
    }

    private boolean checkIfDescriptionExits(String description) {
        return getAllCategories().stream().map(CategoryDto::getDescription).collect(Collectors.toList()).contains(description);

    }
}
