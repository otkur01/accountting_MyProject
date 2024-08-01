package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDto getById(Long id);

    List<InvoiceProductDto> getAllByInvoiceId(Long id);

    List<InvoiceProductDto> getAllByProductId(Long id);


}
