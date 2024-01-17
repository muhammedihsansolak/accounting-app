package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class ClientVendorDTO {

    private Long id;

    @NotBlank(message = "Company Name is required field.")
    @NotNull
    @Size(min = 2, max = 100, message = "Company Name must be between 2 and 50 characters long.")
    private String clientVendorName;

    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @NotBlank(message = "Website should have a valid format")

    @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*",
            message = "Website should have a valid format.")
    private String website;

    private ClientVendorType clientVendorType;

    @Valid
    private AddressDTO address;
    private boolean hasInvoice;


}