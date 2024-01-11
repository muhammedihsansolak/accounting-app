package com.cydeo.controller;

import com.cydeo.dto.ProductDTO;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //End-user should be able to List (display) all products in the product_list page...
    @GetMapping("/list")
    public String listAllProducts(Model model){
        List<ProductDTO> productDTOList = productService.listAllProducts();
        model.addAttribute("products", productDTOList);

        return "/product/product-list";
    }

    //End-user should be able to Edit each product, when click on Edit button, end-user should land on product_update page and the edit form should be populated with the information of that very same product.
    @GetMapping("/update/{id}")
    public String getProductToBeUpdated(@PathVariable("id") Long id, Model model){
        ProductDTO productDTOToBeUpdated = productService.findById(id);
        model.addAttribute("product",productDTOToBeUpdated);

        return "product/product-update";
    }

    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") ProductDTO updatedProduct) {
        productService.update(updatedProduct);

        return
    }


    //When End-User clicks on "Create-Product" button, product_create page should be displayed with an Empty product form,
    @GetMapping("/create")
    public String createProduct(){


        return "/product/product-create";
    }

    //End-user should be able to Delete each product(soft delete), then end up to the product_list page with updated product list.
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Long id, Model model){
        ProductDTO productDTOToBeDeleted = productService.findById(id);
        productService.delete(id);

        return "redirect:/product-create";
    }



}
