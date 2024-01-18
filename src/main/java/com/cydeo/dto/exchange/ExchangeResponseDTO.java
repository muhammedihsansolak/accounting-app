package com.cydeo.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeResponseDTO {

    //The JSON file was saying that we needed to add these objects

    private String date;

    private Usd usd;

}
