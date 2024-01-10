package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invoice_products")
public class InvoiceProduct extends BaseEntity{

     private int quantity;

    private BigDecimal price;

    private int tax;

    private BigDecimal profitLoss;

   private int remainingQuantity;

   @ManyToOne
   @JoinColumn(name = "invoice_id")
    private Invoice invoice;


   @ManyToOne
   @JoinColumn(name = "product_id")
    private Product product;




}
