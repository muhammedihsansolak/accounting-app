package com.cydeo.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeResponseDTO {

    private String date;

    private Usd usd;

}
