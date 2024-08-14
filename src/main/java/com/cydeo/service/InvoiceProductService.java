package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;

import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDto getById(Long id);

    List<InvoiceProductDto> getAllByInvoiceId(Long id);

    List<InvoiceProductDto> getAllByProductId(Long id);


    List<InvoiceProductDto> getAllInvoiceByCompanyId(Long id);

    List<InvoiceProductDto> getAllInvoiceProductByCompanyId(Long id);

    InvoiceProductDto save(InvoiceProductDto invoiceProductDto);

    InvoiceProductDto delete(Long id2);
}
