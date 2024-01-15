package com.cydeo.dto;

import lombok.Getter;
import java.util.Map;

@Getter
public class CountriesDTO {
    private Map<String, CountryInfoDTO> data;
}
