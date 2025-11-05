package com.example.springFirstProject.repository;

import com.example.springFirstProject.Beans.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Products p WHERE " +
            "productStockQuantity > 0 " +
            "AND active = true " +
            "AND (LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchKey, '%'))" +
            "OR LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :searchKey, '%'))" +
            "OR LOWER(p.productCategory) LIKE LOWER(CONCAT('%', :searchKey, '%')))")
    List<Product> findProductBySearchKey(@Param("searchKey") String searchKey);

    Optional<Product> findByproductIdAndActiveTrue(Long productId);
}
