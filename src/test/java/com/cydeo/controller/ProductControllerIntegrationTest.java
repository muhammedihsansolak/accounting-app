package com.cydeo.controller;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private UserPrincipal principal;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUsername("username");
        user.setRole(new Role("Admin"));
        user.setEnabled(true);
        user.setIsDeleted(false);
        user.setPassword("Abc1");
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

}