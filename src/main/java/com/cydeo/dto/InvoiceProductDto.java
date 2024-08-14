package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {


 private    Long id;
    @NotNull(message = "Quantity is a required field")
    @Min(value = 1,message = "Quantity cannot be greater than 100 or less than 1")
    @Max(value = 100, message = "Quantity cannot be greater than 100 or less than 1")
    private   Integer quantity;

    @NotNull(message = "Price is a required field")
    @Min(value = 1, message = "Price should be at least $1")
    private  BigDecimal price;

    @NotNull(message = "Tax is a required field")
    @Min(value = 0, message = "Tax should be between 0% and 20%")
    @Max(value = 20, message = "Tax should be between 0% and 20%")
    private    Integer tax;
    private  BigDecimal total;
    private BigDecimal profitLoss;
    private   Integer remainingQuantity;
    private  InvoiceDto invoice;

    @NotNull(message = "Product is a required field")
    private    ProductDto product;

}
