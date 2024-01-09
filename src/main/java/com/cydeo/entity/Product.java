package com.cydeo.entity;

import com.cydeo.enums.ProductUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
@Where(clause = "is_deleted_false")
public class Product extends BaseEntity{

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category")
//    private Category category;




}
