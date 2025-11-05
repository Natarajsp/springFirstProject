package com.example.springFirstProject.DTO.orderdto;

import com.example.springFirstProject.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderInvoice;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemsDTO> orderItemsDTOList;
    private LocalDateTime createdAt;
}
