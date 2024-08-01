package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final MapperUtil mapperUtil;
    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductServiceImpl(MapperUtil mapperUtil, InvoiceProductRepository invoiceProductRepository) {
        this.mapperUtil = mapperUtil;
        this.invoiceProductRepository = invoiceProductRepository;
    }

    @Override
    public InvoiceProductDto getById(Long id) {
        return mapperUtil.convert(invoiceProductRepository.findById(id).get(), new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> getAllByInvoiceId(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id).stream().map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDto> getAllByProductId(Long id) {
        return invoiceProductRepository.findAllByProductId(id).stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }


}
