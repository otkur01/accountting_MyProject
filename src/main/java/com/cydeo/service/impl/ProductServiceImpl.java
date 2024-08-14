package com.cydeo.service.impl;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Product;
import com.cydeo.enums.ProductUnit;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService, InvoiceProductService invoiceProductService, InvoiceService invoiceService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.invoiceProductService = invoiceProductService;
        this.invoiceService = invoiceService;
    }

    @Override
    public ProductDto getById(Long id) {
        return mapperUtil.convert(productRepository.findById(id).get(), new ProductDto());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        Long LoggedCompanyId = securityService.getLoggedInUser().getCompany().getId();

        return productRepository.findAllOrderByCategoryNameAscAndNameAsc().stream().
                filter(product -> product.getCategory().getCompany().getId().equals(LoggedCompanyId))
                .filter(product -> product.getIsDeleted().equals(false))
                .map(product -> mapperUtil.convert(product, new ProductDto()))
                .map(productDto ->{
                    productDto.setHasInvoiceProduct(!checkProductHasInvoice(productDto.getId()));
                    return productDto;
                })
                .collect(Collectors.toList());
    }

    private boolean checkProductHasInvoice(Long id) {
         return  invoiceProductService.getAllByProductId(id).isEmpty();


    }

    @Override
    public List<ProductDto> getAllProductByCategoryId(Long id) {
        return productRepository.findAllByCategoryId(id).stream()
                .filter(product -> product.getCategory().getId()==id)
                .map(product -> mapperUtil.convert(product,new ProductDto()))
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductDto> getAllProductOrderedByCategoryAndName() {
        return null;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
       Product product = mapperUtil.convert(productDto, new Product());
      productRepository.save(product);
      return mapperUtil.convert(product, new ProductDto());

    }

    @Override
    public List<ProductUnit> getAllProductUnits() {
        List<ProductUnit>productUnits = new ArrayList<>();
        productUnits.add(ProductUnit.FEET);
        productUnits.add(ProductUnit.KG);
        productUnits.add(ProductUnit.LBS);
        productUnits.add(ProductUnit.INCH);
        productUnits.add(ProductUnit.GALLON);
        productUnits.add(ProductUnit.METER);
        productUnits.add(ProductUnit.PCS);
        return productUnits;
    }

    @Override
    public ProductDto update(ProductDto productDto) {
          Product product = mapperUtil.convert(productDto, new Product());
          productRepository.save(product);
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> getAllProductForLoggedCompanySortedByCategoriesAndName() {
        return productRepository.findAllByCompanyIdOrderByCategoryNameAscAndNameAsc(securityService.getLoggedInUser().getId())
                .stream().map(product -> mapperUtil.convert(product, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Product product= productRepository.findById(id).get();
        if (product.getQuantityInStock()>0)throw new RuntimeException("cant delete this product");
        product.setIsDeleted(true);
        productRepository.save(product);
    }
}
