package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    void save(InvoiceDto invoiceDto);
    String generateInvoiceNo();
   // List<InvoiceDto> listAllByCompanyAndInvoiceType(Company company, InvoiceType invoiceType);
    List<InvoiceDto> listAllByCompanyAndInvoiceType(CompanyDto companyDto, InvoiceType invoiceType, InvoiceStatus invoiceStatus);
    List<InvoiceDto> listAllPurchaseInvoices(InvoiceType invoiceType);

    InvoiceDto getById(Long id);

    List<InvoiceDto>getInvoiceByCompanyAndInvoiceStatus(InvoiceStatus invoiceStatus, CompanyDto companyDto);

    List<InvoiceDto> getAllInvoiceDtoByClientId(Long id);

    List<InvoiceDto> getAllByCompanyId(Long id);
}