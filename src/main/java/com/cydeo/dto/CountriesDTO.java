package com.cydeo.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CountriesDTO {
    private List<CountryInfoDTO> alpha2Code;
}
