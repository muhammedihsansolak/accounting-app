package com.cydeo.converter;

import com.cydeo.dto.CategoryDTO;
import com.cydeo.entity.Category;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class CategoryDTOConverter implements Converter <String, CategoryDTO> {

    CategoryService categoryService;

    public CategoryDTOConverter(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @Override
    public CategoryDTO convert(String id) {
        Long source = Long.valueOf(id);
        return categoryService.findById(source);

    }


}

