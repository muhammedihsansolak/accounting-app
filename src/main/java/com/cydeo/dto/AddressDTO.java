package com.cydeo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;

    @NotBlank(message = "Address is a required field.")
    @Size(min = 2, max = 100, message = "Address should have 2-100 characters long.")
    private String addressLine1;

    @Size(max = 100, message = "Address should have maximum 100 characters long.")
    private String addressLine2;

    @NotBlank(message = "City is a required field.")
    @Size(min = 2, max = 50, message = "City should have 2-50 characters long.")
    private String city;

    @NotBlank(message = "State is a required field.")
    @Size(min = 2, max = 50, message = "State should have 2-50 characters long.")
    private String state;

    @NotBlank(message =  "Country is a required field.")
    @Size(message ="Country should have 2-50 characters long.")
    private String country;

    @NotBlank(message = "Zipcode is a required field.")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Please enter a valid zipcode. It should be in the format of 5 digits, optionally followed by a hyphen and 4 more digits (e.g., 12345 or 12345-6789).")
    private String zipCode;

}