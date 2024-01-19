package com.cydeo.dto.exchange;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Usd {


    //In the exchange table, we only want to see these currencies.

    private BigDecimal eur;
    private BigDecimal gbp;
    private BigDecimal cad;
    private BigDecimal jpy;
    private BigDecimal inr;


    //The names of the getter methods were adjusted according to the lines in dashboard.html

    public BigDecimal getEuro() {
        return eur;
    }

    public BigDecimal getBritishPound() {
        return gbp;
    }

    public BigDecimal getCanadianDollar() {
        return cad;
    }

    public BigDecimal getJapaneseYen() {
        return jpy;
    }

    public BigDecimal getIndianRupee() {
        return inr;
    }

}
