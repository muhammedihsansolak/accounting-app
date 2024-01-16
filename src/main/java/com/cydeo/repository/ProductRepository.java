package com.cydeo.repository;

import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.enums.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByCategory(Category category);

}
