package com.example.springFirstProject.Beans;

import com.example.springFirstProject.Enums.OrderStatus;
import com.example.springFirstProject.Enums.PaymentMethod;
import com.example.springFirstProject.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;

    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private BigDecimal shippingCharge;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.UPI_PAYMENT;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String orderInvoice;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }


    // Calculate the Total Amount method
    public BigDecimal calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                if (orderItem != null) {
                    total = total.add(orderItem.calculatedTotalAmount());
                }
            }
        }

        // Handle nulls safely
        BigDecimal tax = this.taxAmount != null ? this.taxAmount : BigDecimal.ZERO;
        BigDecimal shipping = this.shippingCharge != null ? this.shippingCharge : BigDecimal.ZERO;
        BigDecimal discountValue = this.discount != null ? this.discount : BigDecimal.ZERO;

        this.totalAmount = total
                .add(tax)
                .add(shipping)
                .subtract(discountValue);

        return this.totalAmount;
    }

    // Generate the Invoice for each Order
    @PrePersist
    public void generateInvoice() {
        if (this.orderInvoice == null) {
            this.orderInvoice = "INV-" + System.currentTimeMillis(); // Example: INV-1694523412345
        }
    }

}
