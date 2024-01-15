package com.cydeo.FeinClient;

import com.cydeo.dto.CountriesDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(url = "https://countryapi.io/api/all", name = "COUNTRY-CLIENT")
public interface CountryClient {
    @GetMapping("")
    ResponseEntity<CountriesDTO> getCountries(@RequestHeader("Authorization") String authorizationHeader);
}
