package com.example.springFirstProject.DTO.orderdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDTO {
    private String productName;
    private Integer productQuantity;
    private BigDecimal totalPrice;
}
