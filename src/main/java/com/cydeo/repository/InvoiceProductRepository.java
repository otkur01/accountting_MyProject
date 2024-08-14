package com.cydeo.repository;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct>findAllByInvoiceId(Long id);
    List<InvoiceProduct> findAllByProductId(Long id);

    @Query("SELECT i FROM InvoiceProduct i WHERE i.invoice.company.id = ?1 AND i.invoice.invoiceType='SALES' AND i.invoice.invoiceStatus='APPROVED'")
    List<InvoiceProduct> findAllSalesInvoiceProduct(Long companyId);
    List<InvoiceProduct> findAllByInvoice_CompanyId(Long id);
}
