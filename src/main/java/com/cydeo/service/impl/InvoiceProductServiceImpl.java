package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final MapperUtil mapperUtil;
    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(MapperUtil mapperUtil, InvoiceProductRepository invoiceProductRepository, @Lazy InvoiceService invoiceService) {
        this.mapperUtil = mapperUtil;
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceProductDto getById(Long id) {
        return mapperUtil.convert(invoiceProductRepository.findById(id).orElseThrow(), new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> getAllByInvoiceId(Long id) {
        return invoiceProductRepository.findAllByInvoiceId(id)
                .stream().filter(invoiceProduct -> invoiceProduct.getIsDeleted().equals(false))
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .map(invoiceProductDto -> {
                    invoiceProductDto.setTotal(setTotalForInvoiceProduct(invoiceProductDto));
                    return invoiceProductDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDto> getAllByProductId(Long id) {
        return invoiceProductRepository.findAllByProductId(id).stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .map(invoiceProductDto -> {
                    invoiceProductDto.setTotal(setTotalForInvoiceProduct(invoiceProductDto));
                    return invoiceProductDto;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal setTotalForInvoiceProduct(InvoiceProductDto invoiceProductDto) {
       BigDecimal totalWithoutTax= invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()));
       BigDecimal TaxAmount = totalWithoutTax.multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
       return totalWithoutTax.add(TaxAmount);
    }

    @Override
    public List<InvoiceProductDto> getAllInvoiceByCompanyId(Long id) {
        return invoiceProductRepository.findAllByInvoice_CompanyId(id).stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getInvoiceType().equals(InvoiceType.SALES))
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .map(invoiceProductDto -> {
                    invoiceProductDto.setTotal(BigDecimal.valueOf(invoiceProductDto.getQuantity())
                            .multiply(invoiceProductDto.getPrice())
                            .multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                    return invoiceProductDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDto> getAllInvoiceProductByCompanyId(Long id) {
        return invoiceProductRepository.findAllByInvoice_CompanyId(id).stream()
                .filter(invoiceProduct -> invoiceProduct.getInvoice().getInvoiceType().equals(InvoiceType.PURCHASE))
                .sorted(Comparator.comparing(BaseEntity::getLastUpdateDateTime).reversed())
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .filter(invoiceProductDto -> invoiceProductDto.getInvoice() != null)
                .map(invoiceProductDto -> {
                    invoiceProductDto.setTotal(BigDecimal.valueOf(invoiceProductDto.getQuantity())
                            .multiply(invoiceProductDto.getPrice())
                            .multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                    return invoiceProductDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceProductDto save(InvoiceProductDto invoiceProductDto) {
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
         invoiceProductRepository.save(invoiceProduct);
        return mapperUtil.convert(invoiceProduct,new InvoiceProductDto());
    }

    @Override
    public InvoiceProductDto delete(Long id2) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id2).orElseThrow();
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
        return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
    }


}
