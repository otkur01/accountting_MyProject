package com.cydeo.repository;

import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long > {

    List<Product> findAllByCategoryId(Long id);

    @Query("SELECT p FROM Product p ORDER BY p.category.description ASC, p.name ASC")
    List<Product> findAllOrderByCategoryNameAscAndNameAsc();

    @Query("SELECT p FROM Product p JOIN InvoiceProduct ip ON p.id = ip.product.id JOIN Invoice i ON ip.invoice.id = i.id WHERE i.company.id = :companyId ORDER BY p.category.description ASC, p.name ASC")
    List<Product> findAllByCompanyIdOrderByCategoryNameAscAndNameAsc(@Param("companyId") Long companyId);





}
