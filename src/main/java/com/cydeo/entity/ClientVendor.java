package com.cydeo.entity;


import enums.ClientVendorType;

import javax.persistence.*;

@Entity
@Table(name = "clients_vendors")
public class ClientVendor {

    private String ClientVendorName;
    private long phone;
    private String website;
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    public void setId(Class<?> aClass) {
    }

    @Entity
     class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    }
    @OneToOne//annotation api ---one to one relationship
    @JoinColumn(name = "adress.id")
    private Address adress;


    @Entity
    class Company {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    }

    @ManyToOne
    @JoinColumn(name = "company.id")
    private Company company;


}
