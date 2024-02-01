package com.cydeo.repository;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findById(Long id);

    List<Product> findAll();
    @Query("SELECT p FROM Product p " +
            "JOIN Category ca ON p.category.id = ca.id " +
            "JOIN Company c ON ca.company.id = c.id " +
            "WHERE c.id = :companyId")
    List<Product> findAllByCompanyId(Long companyId);

    List<Product> findAllByCategory_CompanyAndQuantityInStockGreaterThan(Company company, int quantity);

    boolean existsByNameAndCategory_IdAndCategory_Company_Id(String productName, Long categoryId, Long companyId);

    List<Product> findByCategory(Category category);
}