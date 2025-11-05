package com.example.springFirstProject.repository;

import com.example.springFirstProject.Beans.CartItem;
import com.example.springFirstProject.Beans.Product;
import com.example.springFirstProject.Beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    @Query("SELECT c FROM CartItem c WHERE c.user.userId = :userId ORDER BY updatedAt desc")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.userId = :userId")
    List<CartItem> getCartItem(@Param("userId") Long userId);
}
