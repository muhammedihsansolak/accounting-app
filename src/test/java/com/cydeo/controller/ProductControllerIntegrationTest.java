package com.cydeo.controller;

import com.cydeo.entity.*;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.enums.ProductUnit;
import com.cydeo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Company company;
    private UserPrincipal principal;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp(){
        Role role = new Role("Admin");
        role = roleRepository.save(role);

        company = new Company();
        company.setTitle("Company A");
        companyRepository.save(company);

        category = new Category();
        category.setDescription("Category");
        category.setCompany(company);
        category = categoryRepository.save(category);

        product = new Product();
        product.setName("Product");
        product.setCategory(category);
        product.setLowLimitAlert(10);
        product.setProductUnit(ProductUnit.PCS);
        product = productRepository.save(product);

        user = new User();
        user.setUsername("username");
        user.setRole(role);
        user.setEnabled(true);
        user.setIsDeleted(false);
        user.setPassword("Abc1");
        user.setCompany(company);
        user = userRepository.save(user);

        principal = new UserPrincipal(user);
    }

    @Test
    void should_retrieve_product_list_page_and_display_all_products() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/products/list")
                .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("/product/product-list"))
                .andExpect(model().attributeExists("products"));

    }

    @Test
    void should_return_update_page() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/products/update/"+product.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("/product/product-update"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("lowLimitAlert"))
                .andExpect(model().attributeExists("productUnits"));
    }

    @Test
    void should_update_product_successfully_and_redirect() throws Exception {
        product.setName("Update Product");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", product.getName());
        formData.add("category", String.valueOf(product.getCategory().getId()));
        formData.add("lowLimitAlert", String.valueOf(product.getLowLimitAlert()));
        formData.add("productUnit", product.getProductUnit().name());

        mvc.perform(MockMvcRequestBuilders.post("/products/update/" + product.getId())
                        .params(formData)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/list"));
    }

}