package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductService invoiceProductService;

    private final ProductService productService;

    private final InvoiceService invoiceService;

    private final InvoiceProductRepository invoiceProductRepository;

    private final SecurityService securityService;

    public ReportingServiceImpl(InvoiceProductService invoiceProductService, ProductService productService, InvoiceService invoiceService, InvoiceProductRepository invoiceProductRepository, SecurityService securityService) {
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.invoiceProductRepository = invoiceProductRepository;
        this.securityService = securityService;
    }

    @Override
    public List<InvoiceProductDto> getAllInvoiceProductByCompanyId() {
        return invoiceProductService.getAllInvoiceProductByCompanyId(securityService.getLoggedInUser().getCompany().getId());
    }

    @Override
    public Map<String, BigDecimal> MonthlyProfitLossData() {
        Map<String, BigDecimal> profitLossMap = new HashMap<>();
//        List<InvoiceDto> approvedSalesInvoices = invoiceService.listAllByCompanyAndInvoiceType(companyDto,InvoiceType.SALES,InvoiceStatus.APPROVED).stream()
//                .collect(Collectors.toList());
//
//        for (InvoiceDto invoiceDto : approvedSalesInvoices) {
//            // Calculate the total profit/loss for the current invoice
//            BigDecimal totalForInvoiceProduct = invoiceProductService.getAllByInvoiceId(invoiceDto.getId())
//                    .stream()
//                    .map(InvoiceProductDto::getProfitLoss)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            // Get the month from the invoice date
//            String month = invoiceDto.getDate().getMonth().toString();
//
//            // Add or update the monthProfitLoss map with the calculated total
//            monthProfitLoss.put(month, monthProfitLoss.getOrDefault(month, BigDecimal.ZERO).add(totalForInvoiceProduct));
//        }
//
//        return monthProfitLoss;



       invoiceService.listAllByCompanyAndInvoiceType(securityService.getLoggedInUser().getCompany(), InvoiceType.SALES,InvoiceStatus.APPROVED).stream()
                .map(invoiceDto -> {
                    String month = invoiceDto.getDate().getMonth().toString();
                  invoiceProductService.getAllByInvoiceId(invoiceDto.getId())

                          .stream()
                          .map(invoiceProductDto -> {

                              if (!profitLossMap.containsKey(month)) {
                                  profitLossMap.put(month, invoiceProductDto.getProfitLoss());
                              }
                              profitLossMap.put(month, profitLossMap.get(month).add(invoiceProductDto.getProfitLoss()));

                              return invoiceProductDto;
                          }).collect(Collectors.toList());



          return invoiceDto;
        }).collect(Collectors.toList());


//        List<InvoiceProduct> listOfSoldInvoiceProduct = invoiceProductRepository.findAllSalesInvoiceProduct(securityService.getLoggedInUser().getCompany().getId());
//         List<InvoiceProductDto>invoiceProductDtoList = invoiceProductService.getAllInvoiceByCompanyId(securityService.getLoggedInUser().getCompany().getId());
//
//        for (InvoiceProductDto invoiceProduct : invoiceProductDtoList) {
//            BigDecimal profitLoss = invoiceProduct.getProfitLoss();
//            String month = invoiceProduct.getInvoice().getDate().getMonth().toString();
//            if (!profitLossMap.containsKey(month)) {
//                profitLossMap.put(month, profitLoss);
//            }
//            profitLossMap.put(month, profitLossMap.get(month).add(profitLoss));
//        }


        return profitLossMap;
    }

    }






