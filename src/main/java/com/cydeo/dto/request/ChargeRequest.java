package com.cydeo.dto.request;

import com.cydeo.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest {

    private String description;
    private BigDecimal amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;


}