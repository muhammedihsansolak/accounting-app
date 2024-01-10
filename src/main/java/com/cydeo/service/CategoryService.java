package com.cydeo.service;


import com.cydeo.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(Long id);
   List<CategoryDTO> findAll();


}
