package com.cydeo.service;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.enums.ProductUnit;
import org.springframework.security.access.method.P;

import java.util.List;

public interface ProductService {
    ProductDto getById(Long id);

    List<ProductDto> getAllProducts();
    List<ProductDto> getAllProductByCategoryId(Long id);

    List<ProductDto> getAllProductOrderedByCategoryAndName();

    ProductDto save(ProductDto productDto);

    List<ProductUnit> getAllProductUnits();

     ProductDto update(ProductDto productDto);

     List<ProductDto> getAllProductForLoggedCompanySortedByCategoriesAndName();

    void delete(Long id);
}
