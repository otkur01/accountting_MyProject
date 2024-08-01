package com.cydeo.repository;

import com.cydeo.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct>findAllByInvoiceId(Long id);
    List<InvoiceProduct> findAllByProductId(Long id);



}
