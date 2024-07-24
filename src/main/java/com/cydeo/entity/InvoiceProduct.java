package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice_products")
public class InvoiceProduct extends BaseEntity {

   private int quantity;
    private BigDecimal price;
    private  int tax;
    private  BigDecimal profitLoss;
    private  int remainingQuantity;
    @ManyToOne
    private  Invoice invoice ;
    @ManyToOne
    private  Product product;
}
