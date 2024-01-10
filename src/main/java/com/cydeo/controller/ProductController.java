package com.cydeo.controller;

import com.cydeo.dto.ProductDTO;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public String listAllProducts(Model model){
        List<ProductDTO> productDTOList = productService.listAllProducts();

        return "product/product-list";
    }

    @GetMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model){
        ProductDTO productDTOToBeUpdated = productService.findById(id);


        return "product/product-update";
    }

    @GetMapping("/create")
    public String createProduct(){


        return "/product/product-create";
    }



}
