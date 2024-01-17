package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    @NotNull
    @NotBlank(message = "Category description is a required field.")
    @Size(max = 100, min = 2, message ="Category Name should be between 2 and 50 characters long.")
    private String description;
    private CompanyDTO company;
    private boolean hasProduct;
}