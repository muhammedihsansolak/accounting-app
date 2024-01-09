package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;

public class CompanyDTO {
    private Long id;
    private String  title;

    private String phone;

    private String website;

    private CompanyStatus companyStatus;

    private AddressDTO address;
}
