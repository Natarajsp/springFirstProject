package com.example.springFirstProject.DTO.cartdto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer productQuantity;
}
