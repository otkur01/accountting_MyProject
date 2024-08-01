package com.cydeo.converter;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.service.InvoiceService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class InvoiceDtoConverter implements Converter<String, InvoiceDto> {

    private final InvoiceService invoiceService;

    public InvoiceDtoConverter(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDto convert(String source) {
        if (source == null || source.equals("")) {
            return null;
        }
        return invoiceService.getById(Long.valueOf(source));
    }
    }

