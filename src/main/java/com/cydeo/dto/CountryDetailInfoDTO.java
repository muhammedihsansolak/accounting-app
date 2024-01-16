package com.cydeo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountryDetailInfoDTO {
    private String name;
    private String iso3;
    private String iso2;
    private String phonecode;
    private String capital;
    private String currency;
    private String emoji;
    private String emojiU;
}
