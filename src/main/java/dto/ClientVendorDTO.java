package dto;

import enums.ClientVendorType;


class CompanyDto {
}
class AddressDto{
}


public class ClientVendorDTO {

    private long id;
    private String clientVendorName;
    private String phone;
    private String website;
    private ClientVendorType clientVendorType;
    private AddressDto address;
    private boolean hasInvoice;

}
