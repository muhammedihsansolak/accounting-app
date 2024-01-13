package com.cydeo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InvoiceProductDTO {

    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private Integer tax;

    private BigDecimal total;

    private BigDecimal profitLoss;

    private Integer remainingQuantity;

    private InvoiceDTO invoice;

    private ProductDTO product;


}
