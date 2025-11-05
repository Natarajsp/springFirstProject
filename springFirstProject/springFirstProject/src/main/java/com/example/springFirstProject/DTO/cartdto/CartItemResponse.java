package com.example.springFirstProject.DTO.cartdto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {

    private String productName;
    private String productDescription;
    private String productCategory;
    private Integer productQuantity;
    private BigDecimal productPrice;

}
