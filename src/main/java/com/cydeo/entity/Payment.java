package com.cydeo.entity;

import com.cydeo.enums.Months;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    private int year;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private boolean isPaid;
    private String companyStripeId;
    
    @Enumerated(EnumType.STRING)
    private Months month;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


}
