package com.example.springFirstProject.Beans;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity(name = "orderItem")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    private Integer productQuantity;

    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    public BigDecimal calculatedTotalAmount() {
        this.totalPrice = product.getProductPrice().multiply(BigDecimal.valueOf(productQuantity));
        return this.totalPrice;
    }
}
