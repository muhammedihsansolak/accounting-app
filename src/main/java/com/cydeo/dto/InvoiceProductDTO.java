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

    private BigDecimal total = price.add(
            price.multiply(BigDecimal.valueOf(tax))
                    .divide( BigDecimal.valueOf(100), RoundingMode.CEILING) );

    private BigDecimal profitLoss;

    private Integer remainingQuantity;

    private InvoiceDTO invoice;

    private ProductDTO product;


}
