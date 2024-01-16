package com.cydeo.FeinClient;

import com.cydeo.dto.CountryDetailInfoDTO;
import com.cydeo.dto.CountryInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Component
@FeignClient(url = "https://api.countrystatecity.in/v1", name = "COUNTRY-CLIENT") // more info : https://countrystatecity.in/docs/api/all-countries/
public interface CountryClient {
    @GetMapping("/countries")
    ResponseEntity<List<CountryInfoDTO>> getCountries(@RequestHeader("X-CSCAPI-KEY") String api_key);

    @GetMapping("/countries/{ciso}")
    ResponseEntity<CountryDetailInfoDTO> getCountryWithDetail(@RequestHeader("X-CSCAPI-KEY") String api_key,
                                                                    @PathVariable("ciso") String ciso);
}
