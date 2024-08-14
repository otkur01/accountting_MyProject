package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    InvoiceDto save(InvoiceDto invoiceDto);
    String generateInvoiceNo();
    String generateInvoiceNoForSale();

   // List<InvoiceDto> listAllByCompanyAndInvoiceType(Company company, InvoiceType invoiceType);
    List<InvoiceDto> listAllByCompanyAndInvoiceType(CompanyDto companyDto, InvoiceType invoiceType, InvoiceStatus invoiceStatus);
    List<InvoiceDto> listAllPurchaseInvoices(InvoiceType invoiceType);
    List<InvoiceDto> listAllSalesInvoices(InvoiceType invoiceType);

    InvoiceDto getById(Long id);

    List<InvoiceDto>getInvoiceByCompanyAndInvoiceStatus(InvoiceStatus invoiceStatus, CompanyDto companyDto);

    List<InvoiceDto> getAllInvoiceDtoByClientId(Long id);

    List<InvoiceDto> getAllByCompanyId(Long id);
    BigDecimal getTotalForInvoice(List<InvoiceProductDto> invoiceProductDtoList);

    BigDecimal getTotalTaxForInvoice(List<InvoiceProductDto> invoiceProductDtoList);

    BigDecimal getTotalPriceForInvoice(List<InvoiceProductDto> invoiceProductDtoList);

    InvoiceDto approve(InvoiceDto invoiceDto);

    void delete(Long id);

    InvoiceDto approveSales(InvoiceDto invoiceDto);

    BigDecimal totalPriceForInvoice(InvoiceDto invoiceDto);
    BigDecimal totalTaxForInvoice(InvoiceDto invoiceDto);
    BigDecimal totalForInvoice(InvoiceDto invoiceDto);
}