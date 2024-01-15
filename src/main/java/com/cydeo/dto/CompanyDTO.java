package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "Title is a required field.")
    @Size(min = 2, max = 100, message = "Title should be 2-100 characters long.")
    private String title;

    @NotBlank(message = "Phone Number is a required field.")
    @Pattern(regexp = "(\\+\\d{1,3}( )?)?\\(?(\\d{3})\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Phone number is required field and may be in any valid phone number format.")
    private String phone;

    @NotBlank(message = "Website is a required field.")
    @Pattern(regexp = "^https?://[a-zA-Z0-9/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*", message = "Website should have a valid format.")
    private String website;

    private CompanyStatus companyStatus;

    @Valid
    private AddressDTO address;
}
