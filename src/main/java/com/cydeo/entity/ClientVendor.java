package com.cydeo.entity;


import enums.ClientVendorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients_vendors")
public class ClientVendor extends BaseEntity {

    private String clientVendorName;
    private String phone;
    private String website;



    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;


    @OneToOne//annotation api ---one to one relationship
    @JoinColumn(name = "address_id")
    private Address adress;



    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


}
